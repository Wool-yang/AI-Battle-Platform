<template>
	<div class="container mt-4">
		<div class="card">
			<div class="card-header" style="font-size: large;">
				All Games
			</div>
			<div class="card-body">
				<div class="row">
					<div class="col-4">
						<div class="card">
							<div class="card-header">
								Snake
							</div>
							<div class="card-body  cd-body">
								<img src="@/assets/images/snake.jpg" alt="">
								<button @click="to_snake" type="button" class="btn btn-primary">play</button>
							</div>
						</div>
					</div>
					<div class="col-4">
						<div class="card">
							<div class="card-header">
								More...
							</div>
							<div class="card-body cd-body">
								<div>敬请期待</div>
							</div>
						</div>
					</div>
					<div class="col-4">
						<div class="card">
							<div class="card-header">
								More...
							</div>
							<div class="card-body cd-body">
								<div>敬请期待</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>
	
<script>
	import router from '@/router'

	export default {
		name: "GamesContainer",
		components: {
		},
		setup() {
			const to_snake = () => {
				router.push({name: "pk_index"});
			};
			
			return {
				to_snake
			}
		}
	};
</script>

<style scoped>
	img {
		width: 70%;
	}
	.cd-body {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}
	button {
		margin-top: 5px;
	}
</style>
    