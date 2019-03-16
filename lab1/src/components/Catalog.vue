<template>
	<div>
		<div class="jumbotron">
			<h3 class="display-3">Learn on demand</h3>
			<p class="lead">Study any topic, anytime. Explore thousands of courses starting at $12.99 each.</p>
			<hr class="my-4">
			<p>Select a category to begin.</p>
		</div>
		<div class="wrap">
			<div class="row">
				<div class="col">
					<label for="categories">Categories</label>
					<select id="categories" class="custom-select" v-model="category">
						<option value="" disabled selected>Choose a category</option>
						<option v-for="(item, k) in coursesJSON" :key="k" :value="k">{{k}}</option>
					</select>
				</div>
				<div class="col" v-if="category">
					<label for="courses">Subjects</label>
					<select id="courses" class="custom-select" v-model="subject" v-on:change="getCourses">
						<option value="" disabled selected>Choose a subject</option>
						<option v-for="(item, k) in coursesJSON[category]" :key="k" :value="k">{{k}}</option>
					</select>
				</div>
			</div>

			<div class="row courses">
				<div class="row cards" v-for="row in Math.ceil(Object.keys(courses).length / 2)">
					<div class="card col-6" v-for="course in courses.slice((row - 1) * 2, row * 2)">
						<img class="card-img-top" :src="`assets/img/${course.src}.jpg`" alt="Course thumbnail">
						<div class="card-body">
							<h5 class="card-title">{{course.name}}</h5>
							<p class="card-text">{{course.desc}}</p>
							<div class="row">
								<div class="col">
									<ul>
										<li><span>Instructor</span>: {{course.instructor}}</li>
										<li><span>Students Enrolled</span>: {{course.students}}</li>
										<li><span>Rating</span>: {{course.rating}}</li>
										<li v-if="course.bestSeller">Best Seller!</li>
										<li><span>Price</span>: ${{course.price}}</li>
									</ul>
								</div>
							</div>
						</div>
						<div class="card-footer" @click="addCourse(course, category, subject)">
							<button class="btn btn-outline-success">
								Add to <i class="fas fa-cart-plus"></i>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		name: "Search",
		props: ['coursesJSON'],
		data() {
			return {
				category: '',
				subject: '',
				courses: {}
			}
		},
		methods: {
			getCourses() {
				if (this.category && this.subject) {
					this.courses = this.coursesJSON[this.category][this.subject]
				}
			},
			addCourse(course, category, subject) {
				course.category = category;
				course.subject = subject;

				this.$emit('add-course', course);
			}
		}
	}
</script>

<style scoped>
	.jumbotron {
		padding: 4rem 2rem 1rem 2rem;
		margin-bottom: 1rem;
	}

	.wrap {
		padding: 0 2rem;
	}

	button[type=submit] {
		position: absolute;
		right: 0;
		margin-right: 15px;
		background: #343a40;
	}

	.courses {
		margin-top: 1rem;
	}

	.card {
		box-sizing: border-box;
		background: #f7f7f9;
		padding: 0;
	}

	.cards {
		margin: .5rem 1rem .5rem .5rem;
	}

	li span {
		font-weight: bold;
		text-decoration: underline;
	}

	.card-footer {
		padding: 0;
	}

	.card-footer .btn {
		width: 100%;
		height: 100%;
	}

	ul li:nth-of-type(4) {
		font-weight: bold;
	}
</style>
