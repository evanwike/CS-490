<template>
	<div>
		<div class="jumbotron">
			<h3 class="display-3">Shopping Cart</h3>
			<p class="lead">{{username}}</p>
			<hr class="my-4">
			Review items in your cart before purchasing.
		</div>
		<div class="wrap">
			<table class="table" v-if="userCourses.length > 0">
				<thead>
				<tr>
					<th scope="col"></th>
					<th scope="col">Category</th>
					<th scope="col">Course</th>
					<th scope="col">Instructor</th>
					<th scope="col">Price</th>
					<th width="20px"></th>
				</tr>
				</thead>
				<tbody>
				<tr v-for="(course, i) in userCourses">
					<td><img :src="`assets/img/${course.src}.jpg`"></td>
					<td>{{course.category}} > {{course.subject}}</td>
					<td>{{course.name}}</td>
					<td>{{course.instructor}}</td>
					<td>${{course.price}}</td>
					<td><i class="fas fa-trash-alt" @click="remove(i)"></i></td>
				</tr>
				<tr>
					<td colspan="3"></td>
					<th scope="col">Subtotal</th>
					<td>${{subtotal}}</td>
				</tr>
				<tr>
					<td colspan="4"></td>
					<td><button class="btn btn-outline-success" @click="$emit('confirm-order')">Check Out</button></td>
				</tr>
				</tbody>
			</table>

			<div id="msg" v-if="userCourses.length === 0">
				<h3>Your shopping cart appears to be empty</h3>
				<p>To add courses to your shopping cart, please visit our course catalog!</p>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		name: "Cart",
		props: [
			'username',
			'userCourses'
		],
		methods: {
			remove(i) {
				this.$emit('remove-course', i)
			}
		},
		computed: {
			subtotal() {
				// Get sum of prices in courses
				const sum = this.userCourses.map(x => x.price).reduce((a, b) => a + b);
				// Round to two decimal places
				return Math.round(sum * 100) / 100
			}
		}
	}
</script>

<style scoped>
	.jumbotron {
		padding: 4rem 2rem 1rem 2rem;
		margin-bottom: 1rem;
	}

	.list-group .active {
		background: #343a40;
	}

	.wrap {
		padding: 0 2rem;
	}

	li {
		cursor: pointer;
	}

	li:hover {
		text-decoration: underline;
	}

	table {
		table-layout: fixed;
	}

	.table td, .table th {
		padding: .5rem;
		vertical-align: middle;
		text-align: center;
	}

	td img {
		width: 100%;
	}

	i {
		cursor: pointer;
	}

	i:hover {
		opacity: .7;
	}

	#msg {
		text-align: center;
	}
</style>
