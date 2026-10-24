# Developer Task Brief: Student Record Data Integrity Verification

## Background

The Student Records application now allows a user to create, edit, reload, and delete student records from a browser page. Before release, the development team needs independent QA evidence that an action on one record does not change or remove another record.

## Your assignment

Perform a manual record-lifecycle test using the synthetic data in this brief. Validate the application through its browser interface.

## Primary risk to test

An update or delete action targets the wrong record, or a saved change disappears after the browser page is reloaded.

## Synthetic test data

| Record | Baseline name | Baseline email | Updated name | Updated email |
| --- | --- | --- | --- | --- |
| A | QA Lifecycle A | qa.lifecycle.a@example.test | QA Lifecycle A Updated | qa.lifecycle.a.updated@example.test |
| B | QA Lifecycle B | qa.lifecycle.b@example.test | Do not change | Do not change |

## Expected evidence

- Before update: A and B are visible with different IDs.
- After updating and reloading: A shows its updated values; B shows its original values.
- After deleting A and reloading: A is absent; B remains with its original values.

## Required workflow

1. Create baseline records A and B from the supplied CSV.
2. Capture the initial state with both records visible, including IDs.
3. Update only record A using the provided updated values.
4. Reload the browser page and confirm A changed as requested while B is identical to its baseline values.
5. Delete only record A.
6. Reload the browser page and confirm A is absent and B remains intact.
7. In IntelliJ IDEA, create a short QA summary that records the observed result and any defect.
8. Record any deviation in the issue log and include it with the final evidence.

## Out of scope

- Real student data
- Performance/load testing
- User authentication/authorization
- Editing database rows manually
- Source-code review during the recording

## Final deliverable after approval

`CRUD-evidence.zip` containing the three screenshots, completed QA summary, and issue log.
