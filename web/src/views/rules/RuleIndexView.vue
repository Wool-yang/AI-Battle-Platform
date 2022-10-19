<template>
	<ContentField>
		<div class="row">
			<div class="col-12">
				<div class="card title1">
					&nbsp;Rules
				</div>
			</div>
		</div>
		<br/>
		<div class="row">
			<div class="col-12">
				<div class="card h-100">
					<div class="card-header title2">
						整体规则
					</div>
					<div class="card-body">
						&emsp;&emsp;你说的对，但是 AI Battle Platform 是由 Wool 写的一个练手平台。该平台致力于让用户在被称作「在线评测系统」的端口编程，在这里，用户的程序将会被作为一局游戏中的「玩家」，来进行对战操作。程序 AI 应当根据游戏的「局面」来做出判断，在不同的游戏中根据不同的规则、用户定义的不同算法，来击败对手的 AI 程序，提高自己的天梯分数——同时，逐步发掘「编程」的乐趣。
						<br/><br/>在 AI Battle Platform 上部署的所有游戏，都遵循一下几点规则: <br/>
						<cite>
							&emsp;&emsp;第一，绝对不意气用事<br/>
							&emsp;&emsp;第二，绝对不漏判任何一件坏事<br/>
							&emsp;&emsp;第三，绝对裁判的公正漂亮<br/>
						</cite>
						<br/>
						在 AI Battle Platform 上编程玩游戏，难道你不觉得这很酷吗？作为一名理工男我觉得这太酷了，很符合我对未来生活的想象，科技并带着趣味
					</div>
				</div>
			</div>
		</div>

		<br/>

		<div class="row">
			<div class="col-12">
				<div class="card">
					<div class="card-header title2">
						具体规则
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
										<button @click="to_page" type="button" class="btn btn-primary">read</button>
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
		</div>

	</ContentField>
</template>
	
<script>
	import ContentField from '@/components/ContentField.vue';

	export default {
		name: "RuleIndex",
		components: {
			ContentField
		},
		setup() {
			const to_page = () => {
				window.open('http://www.baidu.com');
			};

			return {
				to_page
			}
		}
	};
</script>

<style scoped>
	.title1 {
		font-size: xx-large;
		font-weight: 700;
		align-items: center;
	}
	.title2 {
		font-size: larger;
		font-weight: 700;
		align-items: center;
	}
	img {
		width: 50%;
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
    