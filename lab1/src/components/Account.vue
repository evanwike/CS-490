<template>
	<div>
		<div class="jumbotron">
			<h3 class="display-3">Account Details</h3>
			<p class="lead" v-if="showRecent">{{username}}'s three most recent orders.</p>
			<p class="lead" v-if="showHistory">{{username}}'s order history.</p>
			<hr class="my-4">
			<ul class="nav">
				<li class="nav-item" @click="open('Recent')" :class="{active: showRecent}">Recent Orders</li>
				<li class="nav-item" @click="open('History')" :class="{active: showHistory}">Order History</li>
			</ul>
		</div>
		<div class="wrap">
			<div id="recent" v-if="showRecent">
				<table class="table" v-if="orders.length > 0">
					<thead>
					<tr>
						<th scope="col">Date</th>
						<th scope="col" colspan="2">Items</th>
						<th scope="col">Subtotal</th>
					</tr>
					</thead>

					<tbody>
					<tr class="table" v-for="(order, i) in orders.slice(0, 3)">
						<td>{{getOrderDate(i)}} - {{getOrderTime(i)}}</td>
						<td colspan="2">
							<ul>
								<li v-for="course in order.courses">
									{{course.name}}   (${{course.price}})
								</li>
							</ul>
						</td>
						<td>${{order.subtotal}}</td>
					</tr>
					</tbody>
				</table>
			</div>

			<div id="history" v-if="showHistory">
				<table class="table" v-if="orders.length > 0">
					<thead>
					<tr>
						<th scope="col">Date</th>
						<th scope="col" colspan="2">Items</th>
						<th scope="col">Subtotal</th>
					</tr>
					</thead>

					<tbody>
					<tr class="table" v-for="(order, i) in orders">
						<td>{{getOrderDate(i)}} - {{getOrderTime(i)}}</td>
						<td colspan="2">
							<ul>
								<li v-for="course in order.courses">
									{{course.name}}   (${{course.price}})
								</li>
							</ul>
						</td>
						<td>${{order.subtotal}}</td>
					</tr>
					</tbody>
				</table>
			</div>

			<div class="msg" v-if="orders.length === 0 && showRecent">
				<h3>You don't appear to have made any orders recently</h3>
				<p>To place an order, visit your shopping cart and press the "Place Order" button</p>
			</div>

			<div class="msg" v-if="orders.length === 0 && showHistory">
				<h3>Your order history appears to be empty</h3>
				<p>To place an order, visit your shopping cart and press the "Place Order" button</p>
			</div>

		</div>
	</div>
</template>

<script>
	export default {
		name: "Account",
		props: [
			'username',
			'orders'
		],
		data() {
			return {
				showHistory: false,
				showRecent: true
			}
		},
		methods: {
			open(page) {
				['History', 'Recent'].forEach(x => this[`show${x}`] = false);
				this[`show${page}`] = true
			},
			getOrderDate(i) {
				const date = this.orders[i].date;
				return `${date.getMonth() + 1}/${date.getDate()}/${date.getFullYear()}`
			},
			getOrderTime(i) {
				const date = this.orders[i].date;
				let hours = Number(date.getHours());
				hours -= hours > 12 ? 12 : 0;

				return `${hours}:${date.getMinutes()} ${date.getHours() > 12 ? 'PM' : 'AM'}`
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

	.nav {
		padding: 0;
	}

	.nav-item {
		list-style-type: none;
		cursor: pointer;
		opacity: .7;
	}

	.nav-item:hover {
		opacity: 1;
	}

	.active {
		text-decoration: underline;
		opacity: 1;
	}

	.table th {
		padding: .5rem;
	}

	.table tr {
		border-bottom: 2px solid rgba(0,0,0,0.05);
	}

	.table td {
		padding: .5rem;
		vertical-align: middle;
	}

	.table ul {
		padding: 0;
		margin-top: auto;
		margin-bottom: auto;
	}

	.table li {
		list-style-type: none;
	}

	.msg {
		text-align: center;
	}

</style>
