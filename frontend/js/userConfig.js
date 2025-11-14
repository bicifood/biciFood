document.addEventListener("DOMContentLoaded", () => {
    const buttons = document.querySelectorAll(".btn-actualitzar");

    buttons.forEach(btn => {
        btn.addEventListener("click", () => {
            const row = btn.closest(".row");
            const valueCol = row.querySelector(".col-md-7");

            // Si encara no estem editant → convertir a input
            if (!row.classList.contains("editing")) {
                const currentText = valueCol.textContent.trim();

                // Crear input
                const input = document.createElement("input");
                input.type = "text";
                input.classList.add("form-control");
                input.value = currentText;

                // Inserir input
                valueCol.innerHTML = "";
                valueCol.appendChild(input);

                // Canviar estat
                btn.textContent = "Desar";
                row.classList.add("editing");
            } 
            else {
                // Guardar nou valor
                const input = valueCol.querySelector("input");
                const newValue = input.value.trim();

                valueCol.textContent = newValue || "(buit)";
                btn.textContent = "Actualitzar";
                row.classList.remove("editing");
            }
        });
    });
});
