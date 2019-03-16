<template>
	<div id="app">
		<Login v-if="showSignInModal" @close="showSignInModal = false" @sign-in="signIn"></Login>
		<NavBar :username="username" :signedIn="signedIn" :numItems="getCourses.length"
		        @sign-in="navSignIn" @sign-out="navSignOut" @open="open"></NavBar>
		<Catalog v-if="showCatalog" :coursesJSON="courses" @add-course="addCourse"></Catalog>
		<Cart v-if="showCart" :username="username" :userCourses="getCourses"
		      @remove-course="removeCourse" @confirm-order="confirmOrder"></Cart>
		<ConfirmOrder v-if="showConfirmOrderModal" @close="showConfirmOrderModal = false" :userCourses="getCourses"
		              @add-order="addOrder" @empty-cart="emptyCart"></ConfirmOrder>
		<Account v-if="showAccount" :username="username" :orders="getOrders"></Account>
	</div>
</template>

<script>
	import courses from './assets/courses'
	import NavBar from '@/components/NavBar'
	import Login from '@/components/Login'
	import Catalog from '@/components/Catalog'
	import Cart from '@/components/Cart'
	import ConfirmOrder from '@/components/ConfirmOrder'
	import Account from '@/components/Account'

	export default {
		name: 'app',
		components: {
			NavBar,
			Login,
			Catalog,
			Cart,
			ConfirmOrder,
			Account
		},
		data() {
			return {
				courses: courses,
				username: 'Guest',
				signedIn: false,
				showSignInModal: false,
				showConfirmOrderModal: false,
				showCatalog: true,
				showCart: false,
				showAccount: false,
				users: {
					Guest: {
						courses: [],
						orders: []
					}
				}
			}
		},
		methods: {
			navSignIn() {
				this.showSignInModal = true;
			},
			navSignOut() {
				this.username = 'Guest';
				this.signedIn = false;
			},
			open(page) {
				// Closes active page and dynamically opens selected page
				['showCart', 'showCatalog', 'showAccount'].forEach(x => this[x] = false);
				this[`show${page}`] = true
			},
			signIn(username) {
				this.signedIn = true;
				this.showSignInModal = false;
				this.username = username ? username : 'Guest';

				if (!this.users[username]) {
					this.users = {
						...this.users,
						[username]: {
							courses: [],
							orders: []
						}
					}
				}
			},
			addCourse(course) {
				// Copy old object and assign to new reference in order to force DOM update
				const users = {...this.users};
				users[this.username].courses.push(course);

				this.users = users
			},
			removeCourse(i) {
				// Copy old object and assign to new reference in order to force DOM update
				const users = {...this.users};
				users[this.username].courses.splice(i, 1);

				this.users = users
			},
			addOrder(order) {
				// Copy old object and assign to new reference in order to force DOM update
				const users = {...this.users};
				users[this.username].orders.push(order);

				this.users = users;
				this.showConfirmOrderModal = false
			},
			emptyCart() {
				// Copy old object and assign to new reference in order to force DOM update
				const users = {...this.users};
				users[this.username].courses = [];

				this.users = users
			},
			confirmOrder() {
				this.showConfirmOrderModal = true;
			}
		},
		computed: {
			getCourses() {
				return this.users[this.username].courses
			},
			getOrders() {
				return this.users[this.username].orders
			}
		}
	}
</script>
