name: PDS4 Standards Change Request
description: "Tasks related to PDS4 Standards Change Request in design and implementation queue"
title: 'CCB-xxx: Copy SCR title here'
labels: "needs:triage, p.must-have, pending-scr, requirement"
projects: "NASA-PDS/6"
assignees: jordanpadams

body:
  - type: textarea
    id: scr
    attributes:
      label: 🦄 Parent SCR
      description: Link to parent SCR in PDS4 CCB repo
      value: NASA-PDS/PDS4-CCB#xxxx
    validations:
      required: true

  - type: textarea
    id: related
    attributes:
      label: 💡 SCR Summary
      description: Copy-paste the original SCR's **Problem Statement** and **Requested Changes** to provide public overview to proposed changes.
