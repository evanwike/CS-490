console.clear();
const log = console.log.bind(console);

const app = new Vue({
    el: '#app',
    data: {
        user_choice: null,
        comp_choice: null,
        winner: null,
        win_text: null,

        board: {
            rock: {
                src: "assets/rock.jpeg",
                val: 0
            },
            paper: {
                src: 'assets/paper.jpeg',
                val: 1
            },
            scissors: {
                src: 'assets/scissors.jpeg',
                val: 2
            }
        },

        scores: {
            user: {
                text: 'User',
                val: 0
            },
            comp: {
                text: 'Computer',
                val: 0
            },
            tie: {
                text: 'Tie',
                val: 0
            }
        }
    },
    methods: {
        pick(tile) {
            const choices = ['Rock', 'Paper', 'Scissors'];

            this.user_choice = choices[tile];

            const compChoice = Math.floor(Math.random() * 3);
            this.comp_choice = choices[compChoice];

            this.endGame(tile, compChoice);
        },

        endGame(choice, compChoice) {
            let winner = null;   // 0 = Tie, 1 = Computer, 2 = User

            if (choice == compChoice) {
                winner = 'tie';
            }
            else {
                if (choice == 0)
                    winner = compChoice == 1 ? 'comp' : 'user';

                else if (choice == 1)
                    winner = compChoice == 2 ? 'comp' : 'user';

                else
                    winner = compChoice == 0 ? 'comp' : 'user';
            }

            this.winner = this.scores[winner].text;
            this.scores[winner].val++;

            setTimeout(() => {
                this.user_choice = null;
                this.comp_choice = null;
            }, 1000);

            this.win_text = this.winner == 'Tie' ? 'Tie!' : this.winner + ' wins!';
        }
    }
});