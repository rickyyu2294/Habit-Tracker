import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import Form from "./Form";

export default function AddGroupForm() {
    return <Form>Add Group Form</Form>;
}
AddGroupForm.propTypes = {
    groups: PropTypes.array,
    onClose: PropTypes.func,
};
