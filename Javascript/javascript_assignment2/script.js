let currentQuestionIndex = 0;
let score = 0;
let questions = [];
let timer;
const timeLimit = 15;
let answered = false; // Prevents multiple answer changes

// Start the game
async function startGame() {
    let category = document.getElementById("category").value;
    let difficulty = document.getElementById("difficulty").value;

    questions = await fetchQuestions(category, difficulty);
    
    if (questions.length > 0) {
        score = 0;
        currentQuestionIndex = 0;
        answered = false;

        document.getElementById("start-screen").classList.add("hidden");
        document.getElementById("quiz-screen").classList.remove("hidden");

        showQuestion();
    } else {
        alert("Failed to fetch questions. Please wait and try again.");
    }
}

// Fetch questions with async/await and retry mechanism
async function fetchQuestions(category, difficulty, retryCount = 0) {
    let amount = 20;
    const url = `https://opentdb.com/api.php?amount=${amount}&category=${category}&difficulty=${difficulty}&type=multiple`;

    try {
        let response = await fetch(url);

        if (response.status === 429 && retryCount < 3) { // Too Many Requests
            console.warn(`API rate limited. Retrying in 3 seconds... (Attempt ${retryCount + 1})`);
            await new Promise(resolve => setTimeout(resolve, 3000));
            return await fetchQuestions(category, difficulty, retryCount + 1);
        }

        if (!response.ok) {
            throw new Error(`HTTP Error: ${response.status}`);
        }

        let data = await response.json();
        return data.results || [];
    } catch (error) {
        console.error("Error fetching questions:", error);
        return [];
    }
}

// Display the current question
function showQuestion() {
    clearInterval(timer);
    if (currentQuestionIndex >= questions.length) {
        endGame();
        return;
    }

    answered = false; // Reset answer state for new question

    let questionData = questions[currentQuestionIndex];
    let questionText = document.getElementById("question-text");
    let optionsContainer = document.getElementById("options");
    let feedback = document.getElementById("feedback");

    questionText.innerHTML = questionData.question;
    optionsContainer.innerHTML = "";
    feedback.innerHTML = "";

    let answers = [...questionData.incorrect_answers, questionData.correct_answer];
    answers.sort(() => Math.random() - 0.5); // Shuffle answers

    answers.forEach(answer => {
        let button = document.createElement("button");
        button.innerHTML = answer;
        button.onclick = () => checkAnswer(button, answer, questionData.correct_answer);
        optionsContainer.appendChild(button);
    });

    startTimer();
}

// Timer function
function startTimer() {
    let timeLeft = timeLimit;
    document.getElementById("time-left").innerText = `Time left: ${timeLeft}`;

    timer = setInterval(() => {
        timeLeft--;
        document.getElementById("time-left").innerText = `Time left: ${timeLeft}`;

        if (timeLeft <= 0) {
            clearInterval(timer);
            showCorrectAnswer();
        }
    }, 1000);
}

// Check answer and prevent changing it
function checkAnswer(button, selectedAnswer, correctAnswer) {
    if (answered) return; // Prevent multiple changes
    answered = true;
    clearInterval(timer);

    let feedback = document.getElementById("feedback");

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

    // Disable all options after selection
    document.querySelectorAll("#options button").forEach(btn => btn.disabled = true);

    setTimeout(() => {
        currentQuestionIndex++;
        showQuestion();
    }, 500);
}

// Show correct answer if time runs out
function showCorrectAnswer() {
    let questionData = questions[currentQuestionIndex];
    let feedback = document.getElementById("feedback");

    feedback.innerHTML = `Time's up! The correct answer was: ${questionData.correct_answer}. Score: ${score}`;
    feedback.style.color = "red";

    document.querySelectorAll("#options button").forEach(btn => {
        if (btn.innerHTML === questionData.correct_answer) {
            btn.classList.add("correct");
        }
        btn.disabled = true;
    });

    setTimeout(() => {
        currentQuestionIndex++;
        showQuestion();
    }, 1000);
}

// End the game
function endGame() {
    document.getElementById("quiz-screen").classList.add("hidden");
    document.getElementById("end-screen").classList.remove("hidden");
    document.getElementById("final-score").innerText = `Your final score: ${score}/${questions.length}`;
}

// Restart game
function restartGame() {
    document.getElementById("end-screen").classList.add("hidden");
    document.getElementById("start-screen").classList.remove("hidden");
}
