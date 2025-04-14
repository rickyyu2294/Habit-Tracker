import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import Form from "./Form";

export default function EditGroupForm({selectedGroupId}) {
    return <Form>Edit Group Form: {selectedGroupId}</Form>;
}
EditGroupForm.propTypes = {
    selectedGroupId: PropTypes.object.isRequired
};
