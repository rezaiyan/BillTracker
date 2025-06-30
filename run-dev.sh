#!/bin/bash

# Subscription Manager Development Script
# This script starts both the backend server and frontend application

echo "🚀 Starting Subscription Manager Development Environment"

# Function to cleanup background processes on exit
cleanup() {
    echo "🛑 Shutting down development environment..."
    kill $SERVER_PID $FRONTEND_PID 2>/dev/null
    exit 0
}

# Set up signal handlers
trap cleanup SIGINT SIGTERM

# Start the backend server in development mode
echo "📡 Starting backend server (H2 database, with mock data)..."
./gradlew :server:bootRun --args='--spring.profiles.active=dev' &
SERVER_PID=$!

# Wait a moment for the server to start
echo "⏳ Waiting for server to start..."
sleep 7

# Check if server is running
if curl -s http://localhost:3000/api/subscriptions > /dev/null; then
    echo "✅ Backend server is running on http://localhost:3000"
else
    echo "❌ Backend server failed to start"
    cleanup
fi

# Start the frontend application
echo "🖥️  Starting frontend application..."
./gradlew :frontend:run &
FRONTEND_PID=$!

echo "🎉 Development environment is ready!"
echo "   - Backend: http://localhost:3000"
echo "   - Frontend: Desktop application should open shortly"
echo ""
echo "Press Ctrl+C to stop both applications"

# Wait for both processes
wait
