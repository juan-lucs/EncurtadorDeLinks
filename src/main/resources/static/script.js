// Usa a mesma origem de onde a página foi aberta (ex.: http://localhost:8080)
const API_URL = window.location.origin;

const form = document.getElementById("form-link");
const inputUrl = document.getElementById("url");
const resultado = document.getElementById("resultado");
const mensagem = document.getElementById("mensagem");

const linkCurto = document.getElementById("link-curto");
const cliques = document.getElementById("cliques");
const dataCriacao = document.getElementById("data-criacao");
const listaAcessos = document.getElementById("ultimos-acessos");

const botaoCopiar = document.getElementById("copiar");
const botaoAtualizar = document.getElementById("atualizar");

let codigoAtual = null;

// Quantos acessos recentes aparecem na tela
const MAX_ACESSOS = 10;

// Transforma "2026-09-20T21:45:12.123456" em "20/09/2026 21:45:12"
function formatarDataHora(texto) {
    const data = new Date(texto);
    return isNaN(data) ? texto : data.toLocaleString("pt-BR");
}

// Mostra na tela os dados que o backend devolveu (SaidaLinkRequest)
function mostrarDados(link) {
    cliques.textContent = link.cliques ?? 0;
    dataCriacao.textContent = link.dataCriacao ?? "-";

    // mais recentes primeiro, só os últimos MAX_ACESSOS
    const acessos = (link.ultimosAcessos ?? []).slice(-MAX_ACESSOS).reverse();

    listaAcessos.replaceChildren();

    if (acessos.length === 0) {
        const vazio = document.createElement("li");
        vazio.className = "vazio";
        vazio.textContent = "Nenhum acesso ainda";
        listaAcessos.appendChild(vazio);
        return;
    }

    for (const acesso of acessos) {
        const item = document.createElement("li");
        item.textContent = formatarDataHora(acesso);
        listaAcessos.appendChild(item);
    }
}

// Busca as estatísticas SEM contar clique (o GET /link/{codigo} conta e redireciona)
async function buscarEstatisticas() {
    const resposta = await fetch(`${API_URL}/link/${codigoAtual}/status`);

    if (!resposta.ok) {
        throw new Error("Não foi possível atualizar as estatísticas.");
    }

    return resposta.json();
}

// Criar link
form.addEventListener("submit", async (event) => {
    event.preventDefault();

    mensagem.textContent = "";
    resultado.classList.add("hidden");

    try {
        const resposta = await fetch(`${API_URL}/link`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                url: inputUrl.value
            })
        });

        if (!resposta.ok) {
            // O backend devolve um ProblemDetail com o campo "detail"
            const erro = await resposta.json().catch(() => null);
            throw new Error(erro?.detail ?? "Não foi possível encurtar a URL.");
        }

        const link = await resposta.json();

        codigoAtual = link.codigo;

        const urlCurta = `${API_URL}/link/${link.codigo}`;

        linkCurto.textContent = urlCurta;
        linkCurto.href = urlCurta;

        mostrarDados(link);

        resultado.classList.remove("hidden");

    } catch (erro) {
        mensagem.textContent = erro.message;
    }
});


// Atualizar contador (botão)
botaoAtualizar.addEventListener("click", async () => {
    if (!codigoAtual) {
        return;
    }

    try {
        mostrarDados(await buscarEstatisticas());
        mensagem.textContent = "";
    } catch (erro) {
        mensagem.textContent = erro.message;
    }
});


// Copiar link
botaoCopiar.addEventListener("click", async () => {
    try {
        await navigator.clipboard.writeText(linkCurto.href);

        botaoCopiar.textContent = "Copiado!";

        setTimeout(() => {
            botaoCopiar.textContent = "Copiar link";
        }, 1500);

    } catch {
        mensagem.textContent = "Não foi possível copiar o link.";
    }
});


// Atualiza uma vez quando você volta pra esta aba
// (ex.: depois de abrir o link curto em outra aba), sem ficar chamando o servidor toda hora
document.addEventListener("visibilitychange", async () => {
    if (document.hidden || !codigoAtual || resultado.classList.contains("hidden")) {
        return;
    }

    try {
        mostrarDados(await buscarEstatisticas());
    } catch {
        // Não mostra erro para não ficar poluindo a tela
    }
});
