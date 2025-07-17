import React from "react";

interface FormOverlayProps {
    open: boolean;
    onClose: () => void;
    children: React.ReactNode;
}

const overlayStyle: React.CSSProperties = {
    position: 'fixed',
    top: 0,
    left: 0,
    width: '100vw',
    height: '100vh',
    background: 'rgba(0,0,0,0.2)',
    backdropFilter: 'blur(6px)',
    zIndex: 1300,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    pointerEvents: 'auto',
};

const formContainerStyle: React.CSSProperties = {
    boxShadow: '0 8px 32px rgba(0,0,0,0.18)',
    borderRadius: 16,
    background: 'white',
    width: 'auto',
    minWidth: 0,
    maxWidth: '90vw',
    margin: 0,
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'stretch',
    pointerEvents: 'auto',
};

const FormOverlay: React.FC<FormOverlayProps> = ({ open, onClose, children }) => {
    if (!open) return null;
    return (
        <div style={overlayStyle}>
            <div style={formContainerStyle}>
                {children}
            </div>
        </div>
    );
};

export default FormOverlay; 