# Expected Results and Evidence Points

| Checkpoint | Expected result | Evidence to capture |
| --- | --- | --- |
| Baseline | Two records are visible. Each has a different ID, name, and email. | `01-before-two-records.png` |
| Update A | A displays `QA Lifecycle A Updated` and `qa.lifecycle.a.updated@example.test`. B remains `QA Lifecycle B` and `qa.lifecycle.b@example.test`. | `02-after-update-and-reload.png` |
| Delete A | A is absent. B remains visible with exactly its original name and email. | `03-after-delete-and-reload.png` |
| Database cross-check (optional until PostgreSQL QA is ready) | The `student` table contains B only; it contains no row for A or A's updated email. | `04-dbeaver-final-state.png` |

## Pass criteria

Pass the task only when every expected result is true after a manual browser reload. A successful button click alone is not evidence of persistence.

## Failure examples

- B's name or email changes while A is edited.
- A reappears after reload after being deleted.
- A's update disappears after reload.
- The visible ID does not match the record that was edited or deleted.
