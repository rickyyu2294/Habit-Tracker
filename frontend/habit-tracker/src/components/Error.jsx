function Error({error}) {
    return (
        <div className="mb-4">
            {error && <p className="text-red-500">{error}</p>}
        </div>
    )
}

export default Error