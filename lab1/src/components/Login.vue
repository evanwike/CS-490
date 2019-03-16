<template>
	<transition name="modal">
		<div class="modal-mask">
			<div class="modal-wrapper">
				<div class="modal-container">
					<div class="modal-header">
						<h3>
							MOOC Catalog
							<small class="text-muted">please sign in</small>
						</h3>
					</div>

					<div class="modal-body">
						<div class="form-group">
							<label for="username">Username</label>
							<input id="username" type="text" class="form-control" placeholder="Enter username" v-model="username">
						</div>

						<div class="form-group" v-if="!guest">
							<label for="password">Password</label>
							<input id="password" type="password" class="form-control" placeholder="Enter password" v-model="password">
						</div>

						<div class="form-check">
							<label class="form-check-label">
								<input class="form-check-input" type="checkbox" v-model="guest" value="" checked="">
								Sign in as guest
							</label>
						</div>

						<button type="submit" class="btn btn-outline-dark" @click="$emit('sign-in', username)">Sign in</button>
					</div>
				</div>
			</div>
		</div>
	</transition>
</template>

<script>
	export default {
		name: "Login",
		data() {
			return {
				username: '',
				password: '',
				guest: false
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
		width: 400px;
		margin: 0px auto;
		padding: 20px 30px;
		background-color: #fff;
		border-radius: 2px;
		box-shadow: 0 2px 8px rgba(0, 0, 0, .33);
		transition: all .3s ease;
		font-family: Helvetica, Arial, sans-serif;
	}

	.modal-header h3 {
		margin-top: 0;
		color: #42b983;
	}

	.modal-body {
		margin: 0 0;
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

	#username, #password {
		padding: 0.75rem 1rem;
	}

	.form-group, .form-check {
		margin-bottom: 10px;
	}
</style>
