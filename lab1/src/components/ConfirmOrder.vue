<template>
	<transition name="modal">
		<div class="modal-mask">
			<div class="modal-wrapper">
				<div class="modal-container">
					<div class="modal-header">
						<h3>
							Order Details
						</h3>
					</div>

					<div class="modal-body">
						<div class="form-group">
							<h6>Billing Information</h6>
							<div class="row">
								<div class="col">
									<label for="first">First</label>
									<input id="first" type="text" class="form-control" placeholder="First name" v-model="user.billing.firstName">
								</div>
								<div class="col">
									<label for="last">Last</label>
									<input id="last" type="text" class="form-control" placeholder="Last name" v-model="user.billing.lastName">
								</div>
							</div>
						</div>

						<div class="form-group">
							<label for="address">Address</label>
							<input id="address" type="text" class="form-control" placeholder="Address" v-model="user.billing.address">

							<div class="row">
								<div class="col">
									<label for="zip">Zip Code</label>
									<input id="zip" type="text" class="form-control" placeholder="Address" v-model="user.billing.zip">
								</div>
								<div class="col">
									<label for="city">City</label>
									<input id="city" type="text" class="form-control" placeholder="Address" v-model="user.billing.city">
								</div>
								<div class="col">
									<label for="state">State</label>
									<input id="state" type="text" class="form-control" placeholder="Address" v-model="user.billing.state">
								</div>
							</div>

							<label for="phone">Phone #</label>
							<input id="phone" type="text" class="form-control" placeholder="XXX-XXX-XXXX" v-model="user.billing.phone">
						</div>

						<div class="form-group">
							<h6 style="margin-top: 1rem;">Credit Card Information</h6>
							<div class="row">
								<div class="col-6">
									<label for="type">Card Type</label>
									<select id="type" class="custom-select">
										<option value="Visa" selected>Visa</option>
										<option value="MasterCard">MasterCard</option>
										<option value="American Express">American Express</option>
										<option value="Discover">Discover</option>
									</select>
								</div>
								<div class="col-3">
									<label for="expire">Exp. Date</label>
									<input id="expire" type="text" class="form-control" placeholder="MM/YY">
								</div>
								<div class="col-3">
									<label for="cvv">CVV</label>
									<input id="cvv" type="text" class="form-control" placeholder="XXX">
								</div>
							</div>
						</div>

						<div class="form-group">
							<label for="number">Card Number</label>
							<input id="number" type="text" class="form-control" placeholder="XXXX-XXXX-XXXX-XXXX">
						</div>
					</div>

					<div class="modal-footer">
						<div>Your card will be charged: <strong>${{subtotal}}</strong></div>
						<button class="btn btn-outline-success" @click="placeOrder">Place Order</button>
					</div>
				</div>
			</div>
		</div>
	</transition>
</template>

<script>
	export default {
		name: "ConfirmOrder",
		props: ['userCourses'],
		data() {
			return {
				user: {
					billing: {
						firstName: '', lastName: '', address: '', zip: '', city: '', state: '', phone: '',
					}
				}
			}
		},
		methods: {
			placeOrder() {
				const order = {
					courses: [...this.userCourses],
					subtotal: this.subtotal,
					date: new Date()
				};

				this.$emit('add-order', order);
				this.$emit('empty-cart')
			}
		},
		computed: {
			subtotal() {
				// Get sum of prices in orders
				const sum = this.userCourses.map(x => x.price).reduce((a, b) => a + b);
				// Round to two decimal places
				return Math.round(sum * 100) / 100
			}
		}
	}
</script>

<style scoped>
	.modal-mask {
		position: fixed;
		z-index: 9998;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background-color: rgba(0, 0, 0, .5);
		display: table;
		transition: opacity .3s ease;
	}

	.modal-wrapper {
		display: table-cell;
		vertical-align: middle;
	}

	.modal-container {
		width: 600px;
		margin: 0px auto;
		padding: 20px 30px;
		background-color: #fff;
		border-radius: 2px;
		box-shadow: 0 2px 8px rgba(0, 0, 0, .33);
		transition: all .3s ease;
		font-family: Helvetica, Arial, sans-serif;
	}

	.modal-header {
		padding: .5rem 1rem;
	}

	.modal-header h3 {
		margin-top: 0;
		color: #42b983;
	}

	.modal-body {
		margin: 0 0;
		padding-top: .25rem;
	}

	.modal-footer .btn {
		padding: .5rem;
	}

	.modal-footer div {
		margin-right: 1rem;
	}

	.modal-default-button {
		float: right;
	}

	.modal-enter {
		opacity: 0;
	}

	.modal-leave-active {
		opacity: 0;
	}

	.modal-enter .modal-container,
	.modal-leave-active .modal-container {
		-webkit-transform: scale(1.1);
		transform: scale(1.1);
	}

	.table td {
		padding: 1rem;
	}

	.form-group {
		margin-bottom: 0;
	}

	.form-control, select {
		padding: .5rem;
	}

	label {
		margin: 0;
	}

	h3 {
		margin: 0;
	}
</style>
