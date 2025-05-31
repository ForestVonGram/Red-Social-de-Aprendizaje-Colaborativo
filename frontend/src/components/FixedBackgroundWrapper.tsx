import { FC, PropsWithChildren } from 'react'

interface Props extends PropsWithChildren {
    imageUrl: string
}

const FixedBackgroundWrapper: FC<Props> = ({ imageUrl, children }) => {
    return (
        <div className="relative">
            <div
                style={{
                    backgroundImage: `url("${imageUrl}")`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    backgroundRepeat: 'no-repeat',
                    backgroundAttachment: 'fixed',
                }}
                className="fixed inset-0 fade-in-bg z-1"
            >
            </div>
            <div className="relative z-10">
                {children}
            </div>
        </div>
    )
}

export default FixedBackgroundWrapper