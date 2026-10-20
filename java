
const gameData = [
  {
    title: "Бағдаршам әлемі",
    questions: [
      { art: "🚦", q: "Бағдаршамның қызыл түсі нені білдіреді?", a: [["🛑","Тоқта!",true],["🚶","Жүре бер!",false],["🚗","Жылдам жүр!",false]] },
      { art: "🟡", q: "Бағдаршамның сары түсі нені білдіреді?", a: [["⚠️","Назар аудар, сигнал ауысуға дайын!",true],["🏃","Жүгіріп өт!",false],["🎉","Ойнай бер!",false]] },
      { art: "🟢", q: "Жасыл сигнал жанғанда не істеу керек?", a: [["👀","Қауіпсіздікті тексеріп, өтуге болады",true],["🙈","Қарамай өте беру",false],["📱","Телефонға қарау",false]] },
      { art: "🚸", q: "Жолдан қай жерден өткен дұрыс?", a: [["🚶","Жаяу жүргіншілер өткелінен",true],["🚙","Көліктердің арасынан",false],["🏃","Кез келген жерден жүгіріп",false]] },
      { art: "🚦", q: "Жолдан өтер алдында неге назар аудару керек?", a: [["👀","Бағдаршамға және көлік қозғалысына",true],["🎧","Музыкаға ғана",false],["📱","Телефон экранына",false]] }
    ]
  },
  {
    title: "Белгілер құпиясы",
    questions: [
      { art: "🚸", q: "🚸 белгісі нені ескертеді?", a: [["🚶","Жаяу жүргіншілер өткелі",true],["⛽","Жанармай бекеті",false],["🅿️","Тұрақ орны",false]] },
      { art: "🛑", q: "STOP белгісін көргенде не істеу керек?", a: [["✋","Толық тоқтап, жағдайды тексеру",true],["➡️","Жылдам өту",false],["🏃","Жүгіріп кету",false]] },
      { art: "🚲", q: "Велосипед жолын қай белгі білдіреді?", a: [["🚲","Велосипед белгісі",true],["🚌","Автобус белгісі",false],["🏥","Аурухана белгісі",false]] },
      { art: "🚫", q: "Тыйым салатын белгі нені көрсетеді?", a: [["⛔","Белгілі бір әрекетке тыйым салынғанын",true],["✅","Барлық әрекетке рұқсат",false],["🎯","Ойын басталғанын",false]] },
      { art: "🚏", q: "Автобус аялдамасын қалай тануға болады?", a: [["🚌","Қоғамдық көлік аялдамасының белгісінен",true],["✈️","Ұшақ белгісінен",false],["🚲","Велосипед белгісінен",false]] }
    ]
  },
  {
    title: "Қауіпсіздік қаһарманы",
    questions: [
      { art: "⚽", q: "Доп жолға домалап кетті. Не істейсің?", a: [["✋","Тоқтап, ересек адамнан көмек сұраймын",true],["🏃","Доптың артынан жүгіріп кетемін",false],["🚗","Көліктердің арасынан өтемін",false]] },
      { art: "📱", q: "Жолға жақындағанда телефонға не істеген дұрыс?", a: [["👀","Телефонды қойып, жолға назар аудару",true],["📱","Хабарлама оқи беру",false],["🎧","Құлаққапты қатты қою",false]] },
      { art: "🌧️", q: "Жаңбырда көшеден қалай өткен дұрыс?", a: [["☂️","Асықпай, жолды мұқият тексеру",true],["🏃","Көлікке қарамай жүгіру",false],["🙈","Көзді жұмып өту",false]] },
      { art: "🚌", q: "Автобустан түскен соң не істеу керек?", a: [["🚸","Қауіпсіз жерге барып, өткелді пайдалану",true],["🏃","Автобустың алдынан жүгіріп өту",false],["🚗","Көлік арасынан өту",false]] },
      { art: "🚘", q: "Өткелге көлік жақындап келеді. Не істейсің?", a: [["✋","Тоқтап, көліктің тоқтағанын тексеру",true],["🏃","Көліктің алдынан жүгіріп өту",false],["📱","Телефонға қарау",false]] }
    ]
  }
];

const modal = document.getElementById("gameModal");
const modalArt = document.getElementById("modalArt");
const modalQuestion = document.getElementById("modalQuestion");
const modalAnswers = document.getElementById("modalAnswers");
const modalFeedback = document.getElementById("modalFeedback");
const modalNext = document.getElementById("modalNext");
const modalProgress = document.getElementById("modalProgressFill");
const modalKicker = document.getElementById("modalKicker");

let activeGame = 0;
let questionIndex = 0;
let roundScore = 0;
let answered = false;
let completed = new Set();
let totalStars = 0;
let toastTimer;

function showToast(message) {
  const toast = document.getElementById("toast");
  toast.textContent = message;
  toast.classList.add("visible");
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => toast.classList.remove("visible"), 2600);
}

function updateProgress() {
  document.getElementById("totalStars").textContent = totalStars;
  document.getElementById("levelProgress").textContent = `${completed.size} / 3`;
}

function openGame(index) {
  activeGame = index;
  questionIndex = 0;
  roundScore = 0;
  answered = false;

  modal.classList.add("open");
  modal.setAttribute("aria-hidden", "false");
  document.body.classList.add("modal-open");

  renderQuestion();
}

function closeGame() {
  modal.classList.remove("open");
  modal.setAttribute("aria-hidden", "true");
  document.body.classList.remove("modal-open");
}

function renderQuestion() {
  const game = gameData[activeGame];
  const question = game.questions[questionIndex];

  answered = false;
  modalKicker.textContent =
    `0${activeGame + 1} / ${game.title.toLocaleUpperCase("kk-KZ")}`;

  modalArt.textContent = question.art;
  modalQuestion.textContent = question.q;
  modalFeedback.textContent = "";
  modalFeedback.className = "modal-feedback";
  modalNext.disabled = true;

  modalNext.textContent =
    questionIndex === game.questions.length - 1
      ? "Нәтижені көру ★"
      : "Келесі сұрақ →";

  modalProgress.style.width =
    `${(questionIndex / game.questions.length) * 100}%`;

  modalAnswers.replaceChildren();

  const shuffledAnswers = question.a
    .map(answer => ({ answer, sort: Math.random() }))
    .sort((a, b) => a.sort - b.sort)
    .map(item => item.answer);

  shuffledAnswers.forEach((answer, index) => {
    const [emoji, text, correct] = answer;
    const button = document.createElement("button");
    button.className = "modal-answer";
    button.type = "button";

    const icon = document.createElement("span");
    icon.className = "answer-emoji";
    icon.textContent = emoji;

    const label = document.createElement("span");
    label.textContent = text;

    const letter = document.createElement("span");
    letter.className = "answer-letter";
    letter.textContent = String.fromCharCode(65 + index);

    button.append(icon, label, letter);
    button.addEventListener("click", () => chooseAnswer(button, correct));
    modalAnswers.appendChild(button);
  });
}

function chooseAnswer(selectedButton, correct) {
  if (answered) return;
  answered = true;

  const buttons = [...modalAnswers.querySelectorAll("button")];
  buttons.forEach(button => button.disabled = true);

  if (correct) {
    roundScore++;
    selectedButton.classList.add("correct");
    modalFeedback.textContent = "✨ Дұрыс! Жарайсың!";
    modalFeedback.classList.add("good");
  } else {
    selectedButton.classList.add("wrong");
    const correctAnswer = gameData[activeGame]
      .questions[questionIndex].a.find(answer => answer[2]);

    const correctButton = buttons.find(button =>
      button.querySelector(".answer-emoji").textContent === correctAnswer[0] &&
      button.children[1].textContent === correctAnswer[1]
    );

    if (correctButton) correctButton.classList.add("correct");

    modalFeedback.textContent = "💡 Қайта ойланып көр! Дұрыс жауап жасыл түспен белгіленді.";
    modalFeedback.classList.add("bad");
  }

  modalProgress.style.width =
    `${((questionIndex + 1) / gameData[activeGame].questions.length) * 100}%`;

  modalNext.disabled = false;
}

function nextQuestion() {
  if (!answered) return;

  if (questionIndex < gameData[activeGame].questions.length - 1) {
    questionIndex++;
    renderQuestion();
  } else {
    finishGame();
  }
}

function finishGame() {
  const game = gameData[activeGame];
  const firstCompletion = !completed.has(activeGame);

  if (firstCompletion) completed.add(activeGame);

  // Қайталап ойнағанда жұлдыздар қайта есептеледі,
  // сондықтан әр деңгей үшін ең жақсы нәтиже сақталады.
  if (!finishGame.best) finishGame.best = {};
  const previousBest = finishGame.best[activeGame] || 0;
  const improved = Math.max(previousBest, roundScore);

  totalStars += improved - previousBest;
  finishGame.best[activeGame] = improved;

  updateProgress();

  modalProgress.style.width = "100%";
  modalKicker.textContent = "СЕНІҢ НӘТИЖЕҢ";
  modalArt.textContent = roundScore === 5 ? "🏆" : roundScore >= 3 ? "🌟" : "🌱";
  modalQuestion.textContent =
    roundScore === 5 ? "Керемет нәтиже!" :
    roundScore >= 3 ? "Жарайсың! Жақсы жұмыс!" :
    "Жақсы бастама!";

  modalAnswers.replaceChildren();

  const result = document.createElement("div");
  result.style.cssText =
    "text-align:center;padding:15px 0 22px;color:#748399;font-size:14px;line-height:2;";
  result.textContent = `${"★".repeat(roundScore)}${"☆".repeat(5 - roundScore)} · ${roundScore} / 5 дұрыс жауап`;
  modalAnswers.appendChild(result);

  modalFeedback.className = "modal-feedback good";
  modalFeedback.textContent =
    roundScore === 5 ? "🎉 Барлық сұраққа дұрыс жауап бердің!" :
    "💛 Үйренуді жалғастыр! Әр талпыныс маңызды.";

  modalNext.disabled = false;
  modalNext.textContent = "Басқа деңгейді таңдау";
  modalNext.onclick = () => {
    modalNext.onclick = null;
    closeGame();
    document.getElementById("levels").scrollIntoView({ behavior: "smooth" });
  };

  showToast(firstCompletion ? "✨ Жаңа деңгей аяқталды!" : "⭐ Нәтиже жаңартылды!");
}

document.querySelectorAll(".level-button").forEach(button => {
  button.addEventListener("click", () => {
    openGame(Number(button.dataset.game));
  });
});

document.getElementById("closeModal").addEventListener("click", closeGame);
modalNext.addEventListener("click", nextQuestion);

modal.addEventListener("click", event => {
  if (event.target === modal) closeGame();
});

document.addEventListener("keydown", event => {
  if (event.key === "Escape" && modal.classList.contains("open")) {
    closeGame();
  }
});

const navLinks = document.querySelectorAll(".nav-link");
const sections = document.querySelectorAll("#home, #levels, #about");

if ("IntersectionObserver" in window) {
  const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => {
      if (!entry.isIntersecting) return;
      navLinks.forEach(link => {
        link.classList.toggle(
          "active",
          link.getAttribute("href") === `#${entry.target.id}`
        );
      });
    });
  }, { rootMargin: "-25% 0px -60% 0px" });

  sections.forEach(section => observer.observe(section));
}

updateProgress();

