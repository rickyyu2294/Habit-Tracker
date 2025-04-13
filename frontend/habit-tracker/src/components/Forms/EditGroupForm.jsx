import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import Form from "./Form";

export default function EditGroupForm() {
    return <Form>Edit Group Form</Form>;
}
EditGroupForm.propTypes = {
    groups: PropTypes.array,
    onClose: PropTypes.func,
};
