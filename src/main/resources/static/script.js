const form = document.getElementById("linkForm");
const urlInput = document.getElementById("url");
const result = document.getElementById("result");
const shortUrl = document.getElementById("shortUrl");
const clicks = document.getElementById("clicks");
const message = document.getElementById("message");
const refreshButton = document.getElementById("refreshButton");
const copyButton = document.getElementById("copyButton");

let codigoAtual = null;
let intervaloCliques = null;

async function atualizarCliques() {
    if (!codigoAtual) return;

    try {
        const response = await fetch(`/link/${codigoAtual}/status`);
        if (!response.ok) return;

        const link = await response.json();
        clicks.textContent = `Cliques: ${link.cliques}`;
    } catch (error) {
        // se falhar, mantém o valor que já estava na tela
        print("aaaaaaaa")
    }
}

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    result.classList.add("hidden");
    message.classList.add("hidden");

    const url = urlInput.value.trim();

    try {
        const response = await fetch("/link", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ url })
        });

        if (!response.ok) {
            throw new Error("Não foi possível encurtar a URL.");
        }

        const link = await response.json();

        // Seu backend retorna o código gerado no campo "codigo".
        const generatedUrl = `${window.location.origin}/link/${link.codigo}`;

        shortUrl.href = generatedUrl;
        shortUrl.textContent = generatedUrl;
        clicks.textContent = `Cliques: ${link.cliques}`;
        codigoAtual = link.codigo;
        clearInterval(intervaloCliques);                      // evita acumular timers
        intervaloCliques = setInterval(atualizarCliques, 5000);

        result.classList.remove("hidden");
    } catch (error) {
        message.textContent = error.message;
        message.classList.remove("hidden");
    }
});

copyButton.addEventListener("click", async () => {
    await navigator.clipboard.writeText(shortUrl.href);

    const originalText = copyButton.textContent;
    copyButton.textContent = "Copiado!";

    setTimeout(() => {
        copyButton.textContent = originalText;
    }, 1500);
    refreshButton.addEventListener("click", atualizarCliques);
});
