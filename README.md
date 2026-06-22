# Idealland Shrine

```
IdeallandShrineEvents.prayShrine(event => {
  const player = event.player
  const god = event.god
  const godId = event.godId
  const before = event.beforeBelief
  const after = event.afterBelief
})
```

```
IdeallandShrineEvents.prayShrine(event => {
  if (event.godId == 'fire') {
    event.player.tell('你参拜了火神碑')
  }

  if (event.godId == 'death') {
    const delta = event.afterBelief - event.beforeBelief
    event.player.tell(`死神信仰变化: ${delta}`)
  }
})
```
