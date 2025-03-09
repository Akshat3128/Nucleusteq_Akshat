let currentQuestionIndex = 0;
let score = 0;
let questions = [];
let timer;
const timeLimit = 15;
let answered = false; 

// Start the game
async function startGame() {
    const category = document.getElementById("category").value;
    const difficulty = document.getElementById("difficulty").value;

    questions = await fetchQuestions(category, difficulty);
    
    if (questions.length) {
        score = 0;
        currentQuestionIndex = 0;
        answered = false;

        toggleScreens("start-screen", "quiz-screen");
        showQuestion();
    } else {
        alert("Failed to fetch questions. Please try again later.");
    }
}

// Fetch questions with retry mechanism
async function fetchQuestions(category, difficulty, retryCount = 0) {
    const url = `https://opentdb.com/api.php?amount=20&category=${category}&difficulty=${difficulty}&type=multiple`;

    try {
        const response = await fetch(url);

        if (response.status === 429 && retryCount < 3) { // Too Many Requests
            console.warn(`Rate limited. Retrying in 3s... (Attempt ${retryCount + 1})`);
            await new Promise(resolve => setTimeout(resolve, 3000));
            return fetchQuestions(category, difficulty, retryCount + 1);
        }

        if (!response.ok) throw new Error(`HTTP Error: ${response.status}`);

        const data = await response.json();
        return data.results || [];
    } catch (error) {
        console.error("Error fetching questions:", error);
        return [];
    }
}

// Display the current question
function showQuestion() {
    clearInterval(timer);
    if (currentQuestionIndex >= questions.length) return endGame();

    answered = false; 

    const { question, correct_answer, incorrect_answers } = questions[currentQuestionIndex];
    const questionText = document.getElementById("question-text");
    const optionsContainer = document.getElementById("options");
    const feedback = document.getElementById("feedback");

    questionText.innerHTML = question;
    feedback.innerHTML = "";
    optionsContainer.innerHTML = "";

    const answers = shuffle([...incorrect_answers, correct_answer]);

    answers.forEach(answer => {
        const button = document.createElement("button");
        button.innerHTML = answer;
        button.classList.add("option-btn");
        button.onclick = () => checkAnswer(button, answer, correct_answer);
        optionsContainer.appendChild(button);
    });

    startTimer();
}

// Timer function
function startTimer() {
    let timeLeft = timeLimit;
    const timerDisplay = document.getElementById("time-left");

    timerDisplay.innerText = `Time left: ${timeLeft}`;
    timer = setInterval(() => {
        if (--timeLeft <= 0) {
            clearInterval(timer);
            showCorrectAnswer();
        }
        timerDisplay.innerText = `Time left: ${timeLeft}`;
    }, 1000);
}

// Check answer
function checkAnswer(button, selectedAnswer, correctAnswer) {
    if (answered) return;
    answered = true;
    clearInterval(timer);

    const feedback = document.getElementById("feedback");
    const allButtons = document.querySelectorAll("#options button");

    if (selectedAnswer === correctAnswer) {
        score++;
        feedback.innerHTML = `Correct! Score: ${score}`;
        feedback.style.color = "green";
        button.classList.add("correct");
    } else {
        feedback.innerHTML = `Wrong! The correct answer was: ${correctAnswer}. Score: ${score}`;
        feedback.style.color = "red";
        button.classList.add("wrong");
    }

    allButtons.forEach(btn => (btn.disabled = true));

    setTimeout(nextQuestion, 500);
}

// Show correct answer if time runs out
function showCorrectAnswer() {
    const questionData = questions[currentQuestionIndex];
    const feedback = document.getElementById("feedback");

    feedback.innerHTML = `Time's up! The correct answer was: ${questionData.correct_answer}. Score: ${score}`;
    feedback.style.color = "red";

    document.querySelectorAll("#options button").forEach(btn => {
        if (btn.innerHTML === questionData.correct_answer) btn.classList.add("correct");
        btn.disabled = true;
    });

    setTimeout(nextQuestion, 1000);
}

// Move to the next question
function nextQuestion() {
    currentQuestionIndex++;
    showQuestion();
}

// End the game
function endGame() {
    toggleScreens("quiz-screen", "end-screen");
    document.getElementById("final-score").innerText = `Your final score: ${score}/${questions.length}`;
}

// Restart game
function restartGame() {
    toggleScreens("end-screen", "start-screen");
}

// Utility function to shuffle array
function shuffle(array) {
    return array.sort(() => Math.random() - 0.5);
}

// Utility function to toggle screens
function toggleScreens(hide, show) {
    document.getElementById(hide).classList.add("hidden");
    document.getElementById(show).classList.remove("hidden");
}
