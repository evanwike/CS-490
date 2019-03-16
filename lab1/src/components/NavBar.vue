<template>
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
		<a class="navbar-brand" href="#">MOOC</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarColor01"
		        aria-controls="navbarColor01" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="navbarColor01">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item">
					<a class="nav-link link" href="#" @click="open('Catalog')" :class="{active: selected === 'Catalog'}">Catalog</a>
				</li>
			</ul>
			<form class="form-inline my-2 my-lg-0">
				<a class="nav-item link" href="#" @click="open('Account')" :class="{active: selected === 'Account'}">{{username}}</a>
				<a v-if="!signedIn" class="nav-item link" href="#" @click="$emit('sign-in')">Sign In</a>
				<a v-if="signedIn" class="nav-item link" href="#" @click="$emit('sign-out')">Sign Out</a>
				<a class="nav-item link" href="#" @click="open('Cart')" :class="{activeCart: selected === 'Cart'}">
					<i class="fas fa-shopping-cart" ></i>{{numItems > 0 ? numItems : ''}}
				</a>
			</form>
		</div>
	</nav>
</template>

<script>
	export default {
		name: "NavBar",
		props: [
			'username',
			'signedIn',
			'numItems'
		],
		data() {
			return {
				selected: 'Catalog'
			}
		},
		methods: {
			open(page) {
				this.selected = page;
				this.$emit('open', page);
			}
		}
	}
</script>

<style scoped>
	nav {
		padding: 1rem 1rem;
	}

	/* Remove link styling from logo */
	.navbar-brand {
		user-select: none;
		cursor: default;
	}

	/* Override annoying Bootstrap styling */
	.link {
		color: white !important;
		opacity: .5;
	}

	.link:hover {
		opacity: 1;
		text-decoration: underline;
	}

	.active {
		opacity: 1 !important;
		text-decoration: underline !important;
	}

	.activeCart {
		opacity: 1 !important;
	}
</style>
