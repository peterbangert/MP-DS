import os
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

stats = pd.read_csv("simulation_stats.csv")
stats['round'] = stats['round'] +1

DAY = 30
days = range(0,151,15)


def add_day_vline():
	for day in days:
		if day % DAY == 0 and day > 0:
			plt.axvline(day, c='r')
			

# Contacts plot -------------------
plt.figure(figsize=(11,7))
contacts_plot = sns.lineplot(data=stats, x="round", y="contacts")
contacts_plot.set_title("Contacts each Round (30 rounds/day)")
contacts_plot.xaxis.set_ticks(days)

add_day_vline()

fig = contacts_plot.get_figure()
fig.savefig("./plots/contacts_plot.png")


# Health plot -------------------
plt.figure(figsize=(11,7))
melted = pd.melt(stats[['round', 'infections', 'healed']], ['round'],
	var_name="Change in Health",
	value_name="occurence")
	
health_plot = sns.lineplot(data=melted, x="round", y='occurence', hue='Change in Health')
health_plot.set_title("Infections and Recoveries per Round")
health_plot.xaxis.set_ticks(days)

add_day_vline()

fig = health_plot.get_figure()
fig.savefig("./plots/health_plot.png")


# Round time plot -------------------
plt.figure(figsize=(11,7))
round_time_plot = sns.lineplot(data=stats, x="round", y="duration(ms)")
round_time_plot.set_title("Runtime per Round in Milliseconds")
round_time_plot.xaxis.set_ticks(days)

add_day_vline()

fig = round_time_plot.get_figure()
fig.savefig("./plots/round_time_plot.png")
