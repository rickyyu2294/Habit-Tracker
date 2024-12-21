import Error from '../components/Error'

function FormContainer({title, children, error}) {
    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100">
            <form className="bg-white p-8 rounded shadow-md">
                {title && <h1 className="text-2xl font-bold mb-4">{title}</h1>}
                <Error error={error} />
                {children}
            </form>
        </div>
    )
}

export default FormContainer