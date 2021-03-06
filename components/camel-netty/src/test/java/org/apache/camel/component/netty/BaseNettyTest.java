begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|buffer
operator|.
name|ByteBufAllocator
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|util
operator|.
name|ResourceLeakDetector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|BindToRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|AvailablePortFinder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|logging
operator|.
name|log4j
operator|.
name|core
operator|.
name|LogEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
comment|/**  *  */
end_comment

begin_class
DECL|class|BaseNettyTest
specifier|public
class|class
name|BaseNettyTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BaseNettyTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|port
specifier|private
specifier|static
specifier|volatile
name|int
name|port
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|initPort ()
specifier|public
specifier|static
name|void
name|initPort
parameter_list|()
throws|throws
name|Exception
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
block|}
annotation|@
name|BeforeClass
DECL|method|startLeakDetection ()
specifier|public
specifier|static
name|void
name|startLeakDetection
parameter_list|()
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"io.netty.leakDetection.maxRecords"
argument_list|,
literal|"100"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"io.netty.leakDetection.acquireAndReleaseOnly"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|ResourceLeakDetector
operator|.
name|setLevel
argument_list|(
name|ResourceLeakDetector
operator|.
name|Level
operator|.
name|PARANOID
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|verifyNoLeaks ()
specifier|public
specifier|static
name|void
name|verifyNoLeaks
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Force GC to bring up leaks
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
comment|// Kick leak detection logging
name|ByteBufAllocator
operator|.
name|DEFAULT
operator|.
name|buffer
argument_list|(
literal|1
argument_list|)
operator|.
name|release
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|LogEvent
argument_list|>
name|events
init|=
name|LogCaptureAppender
operator|.
name|getEvents
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|events
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|message
init|=
literal|"Leaks detected while running tests: "
operator|+
name|events
decl_stmt|;
comment|// Just write the message into log to help debug
for|for
control|(
name|LogEvent
name|event
range|:
name|events
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
name|event
operator|.
name|getMessage
argument_list|()
operator|.
name|getFormattedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LogCaptureAppender
operator|.
name|reset
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|AssertionError
argument_list|(
name|message
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|getPropertiesComponent
argument_list|()
operator|.
name|setLocation
argument_list|(
literal|"ref:prop"
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|BindToRegistry
argument_list|(
literal|"prop"
argument_list|)
DECL|method|loadProperties ()
specifier|public
name|Properties
name|loadProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|prop
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|prop
operator|.
name|setProperty
argument_list|(
literal|"port"
argument_list|,
literal|""
operator|+
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|prop
return|;
block|}
DECL|method|getNextPort ()
specifier|protected
name|int
name|getNextPort
parameter_list|()
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
return|return
name|port
return|;
block|}
DECL|method|getPort ()
specifier|protected
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|byteArrayToHex (byte[] bytes)
specifier|protected
name|String
name|byteArrayToHex
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|bytes
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%02x"
argument_list|,
name|b
operator|&
literal|0xff
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|fromHexString (String hexstr)
specifier|protected
name|byte
index|[]
name|fromHexString
parameter_list|(
name|String
name|hexstr
parameter_list|)
block|{
name|byte
name|data
index|[]
init|=
operator|new
name|byte
index|[
name|hexstr
operator|.
name|length
argument_list|()
operator|/
literal|2
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|n
init|=
name|hexstr
operator|.
name|length
argument_list|()
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|+=
literal|2
control|)
block|{
name|data
index|[
name|i
operator|/
literal|2
index|]
operator|=
operator|(
name|Integer
operator|.
name|decode
argument_list|(
literal|"0x"
operator|+
name|hexstr
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|+
name|hexstr
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|1
argument_list|)
argument_list|)
operator|)
operator|.
name|byteValue
argument_list|()
expr_stmt|;
block|}
return|return
name|data
return|;
block|}
block|}
end_class

end_unit

