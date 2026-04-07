---
id: ${workflow_id}-review-${sequence}
workflow_id: ${workflow_id}
type: review
agent: reviewer
status: pending
priority: ${priority}
created: ${timestamp}
updated: ${timestamp}
depends_on: [${impl_issue_id}, ${test_issue_id}]
blocks: []
---

# Review ${component_name} Component

## Context

Review implementation and tests for ${component_name} AEM component.

**Implementation**: bmad/gastown/bead/.issues/coder/${impl_issue_id}.md
**Tests**: bmad/gastown/bead/.issues/tester/${test_issue_id}.md
**Specification**: bmad/gastown/bead/.issues/docs/${spec_issue_id}.md

## Review Checklist

### Code Quality

- [ ] Follows AEM coding standards
- [ ] Proper Java annotations used
- [ ] No code smells
- [ ] Proper exception handling

### SEO/GEO Best Practices

- [ ] Correct semantic HTML structure
- [ ] Proper metadata handling
- [ ] JSON-LD properly formatted
- [ ] Accessible markup

### Security

- [ ] No XSS vulnerabilities
- [ ] Proper resource handling
- [ ] No hardcoded secrets

### Performance

- [ ] No blocking operations in getters
- [ ] Lazy loading where appropriate
- [ ] Cache-friendly design

### Testing

- [ ] Tests pass
- [ ] Coverage meets threshold
- [ ] Tests are meaningful

## Review Findings

| Issue | Severity | Location | Description |
|-------|----------|----------|-------------|
| | | | |

## Approval Status

- [ ] Approved
- [ ] Changes Requested
- [ ] Blocked

## Progress Log

### ${timestamp}
Review initiated by ${reviewer_name}.

### ${timestamp}
Review completed.

## Related Issues

- Specification: #${spec_issue_id}
- Implementation: #${impl_issue_id}
- Testing: #${test_issue_id}
