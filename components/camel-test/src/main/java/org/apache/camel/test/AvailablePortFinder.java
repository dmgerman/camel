begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|DatagramSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Finds currently available server ports.  */
end_comment

begin_class
DECL|class|AvailablePortFinder
specifier|public
specifier|final
class|class
name|AvailablePortFinder
block|{
comment|/**      * The minimum server currentMinPort number for IPv4.      * Set at 1100 to avoid returning privileged currentMinPort numbers.      */
DECL|field|MIN_PORT_NUMBER
specifier|public
specifier|static
specifier|final
name|int
name|MIN_PORT_NUMBER
init|=
literal|1100
decl_stmt|;
comment|/**      * The maximum server currentMinPort number for IPv4.      */
DECL|field|MAX_PORT_NUMBER
specifier|public
specifier|static
specifier|final
name|int
name|MAX_PORT_NUMBER
init|=
literal|65535
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AvailablePortFinder
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * We'll hold open the lowest port in this process      * so parallel processes won't use the same block      * of ports.   They'll go up to the next block.      */
DECL|field|LOCK
specifier|private
specifier|static
specifier|final
name|ServerSocket
name|LOCK
decl_stmt|;
comment|/**      * Incremented to the next lowest available port when getNextAvailable() is called.      */
DECL|field|currentMinPort
specifier|private
specifier|static
name|AtomicInteger
name|currentMinPort
init|=
operator|new
name|AtomicInteger
argument_list|(
name|MIN_PORT_NUMBER
argument_list|)
decl_stmt|;
comment|/**      * Creates a new instance.      */
DECL|method|AvailablePortFinder ()
specifier|private
name|AvailablePortFinder
parameter_list|()
block|{
comment|// Do nothing
block|}
static|static
block|{
name|int
name|port
init|=
name|MIN_PORT_NUMBER
decl_stmt|;
name|ServerSocket
name|ss
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|ss
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|ss
operator|=
operator|new
name|ServerSocket
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ss
operator|=
literal|null
expr_stmt|;
name|port
operator|+=
literal|200
expr_stmt|;
block|}
block|}
name|LOCK
operator|=
name|ss
expr_stmt|;
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
operator|new
name|Thread
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|LOCK
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|currentMinPort
operator|.
name|set
argument_list|(
name|port
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets the next available port starting at the lowest number. This is the preferred      * method to use. The port return is immediately marked in use and doesn't rely on the caller actually opening      * the port.      *      * @throws IllegalArgumentException is thrown if the port number is out of range      * @throws NoSuchElementException if there are no ports available      * @return the available port      */
DECL|method|getNextAvailable ()
specifier|public
specifier|static
specifier|synchronized
name|int
name|getNextAvailable
parameter_list|()
block|{
name|int
name|next
init|=
name|getNextAvailable
argument_list|(
name|currentMinPort
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|currentMinPort
operator|.
name|set
argument_list|(
name|next
operator|+
literal|1
argument_list|)
expr_stmt|;
return|return
name|next
return|;
block|}
comment|/**      * Gets the next available port starting at a given from port.      *      * @param fromPort the from port to scan for availability      * @throws IllegalArgumentException is thrown if the port number is out of range      * @throws NoSuchElementException if there are no ports available      * @return the available port      */
DECL|method|getNextAvailable (int fromPort)
specifier|public
specifier|static
specifier|synchronized
name|int
name|getNextAvailable
parameter_list|(
name|int
name|fromPort
parameter_list|)
block|{
if|if
condition|(
name|fromPort
argument_list|<
name|currentMinPort
operator|.
name|get
operator|(
operator|)
operator|||
name|fromPort
argument_list|>
name|MAX_PORT_NUMBER
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"From port number not in valid range: "
operator|+
name|fromPort
argument_list|)
throw|;
block|}
for|for
control|(
name|int
name|i
init|=
name|fromPort
init|;
name|i
operator|<=
name|MAX_PORT_NUMBER
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|available
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"getNextAvailable({}) -> {}"
argument_list|,
name|fromPort
argument_list|,
name|i
argument_list|)
expr_stmt|;
return|return
name|i
return|;
block|}
block|}
throw|throw
operator|new
name|NoSuchElementException
argument_list|(
literal|"Could not find an available port above "
operator|+
name|fromPort
argument_list|)
throw|;
block|}
comment|/**      * Checks to see if a specific port is available.      *      * @param port the port number to check for availability      * @return<tt>true</tt> if the port is available, or<tt>false</tt> if not      * @throws IllegalArgumentException is thrown if the port number is out of range      */
DECL|method|available (int port)
specifier|public
specifier|static
name|boolean
name|available
parameter_list|(
name|int
name|port
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|port
argument_list|<
name|currentMinPort
operator|.
name|get
operator|(
operator|)
operator|||
name|port
argument_list|>
name|MAX_PORT_NUMBER
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid start currentMinPort: "
operator|+
name|port
argument_list|)
throw|;
block|}
name|ServerSocket
name|ss
init|=
literal|null
decl_stmt|;
name|DatagramSocket
name|ds
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ss
operator|=
operator|new
name|ServerSocket
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|ss
operator|.
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ds
operator|=
operator|new
name|DatagramSocket
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|ds
operator|.
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Do nothing
block|}
finally|finally
block|{
if|if
condition|(
name|ds
operator|!=
literal|null
condition|)
block|{
name|ds
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ss
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|ss
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|/* should not be thrown */
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

