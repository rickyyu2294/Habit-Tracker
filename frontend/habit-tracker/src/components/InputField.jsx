import React from "react";

function InputField({ label, type = "text", value, onChange }) {
  return (
    <div className="mb-4">
      <label className="block text-sm font-medium mb-2">{label}</label>
      <input
        type={type}
        value={value}
        onChange={onChange}
        className="w-full px-4 py-2 border rounded"
      />
    </div>
  );
}

export default InputField;
