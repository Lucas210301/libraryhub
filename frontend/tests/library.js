const NAMES = [
  "Ana Beatriz Souza",
  "Carlos Eduardo Lima",
  "Mariana Ferreira",
  "João Pedro Alves",
  "Larissa Gomes",
  "Rafael Monteiro",
  "Juliana Barbosa",
  "Thiago Nascimento"
];

const TITLES = [
  "Dom Casmurro",
  "Grande Sertão: Veredas",
  "Vidas Secas",
  "Capitães da Areia",
  "O Cortiço",
  "A Hora da Estrela",
  "Macunaíma",
  "Iracema",
  "Quarto de Despejo",
  "O Nome da Rosa"
];

const AUTHORS = {
  "Dom Casmurro": "Machado de Assis",
  "Grande Sertão: Veredas": "João Guimarães Rosa",
  "Vidas Secas": "Graciliano Ramos",
  "Capitães da Areia": "Jorge Amado",
  "O Cortiço": "Aluísio Azevedo",
  "A Hora da Estrela": "Clarice Lispector",
  "Macunaíma": "Mário de Andrade",
  "Iracema": "José de Alencar",
  "Quarto de Despejo": "Carolina Maria de Jesus",
  "O Nome da Rosa": "Umberto Eco"
};

function pick(list) {
  return list[Math.floor(Math.random() * list.length)];
}

// Short code that keeps every run distinct, the way a library tells two copies
// of the same book apart.
export function copyCode() {
  return Math.random().toString(36).slice(2, 6).toUpperCase();
}

export function aMember() {
  const name = pick(NAMES);
  const handle = name.toLowerCase().normalize("NFD").replace(/[^a-z ]/g, "").split(" ")[0];
  return { name, email: `${handle}.${copyCode().toLowerCase()}@ufape.edu.br` };
}

export function aBook() {
  const title = pick(TITLES);
  return {
    title: `${title} (exemplar ${copyCode()})`,
    author: AUTHORS[title],
    isbn: `978${Math.floor(Math.random() * 10 ** 10)}`.padEnd(13, "0")
  };
}
