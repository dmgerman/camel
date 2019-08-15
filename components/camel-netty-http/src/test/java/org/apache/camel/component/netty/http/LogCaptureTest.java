begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
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
operator|.
name|http
package|;
end_package

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
name|io
operator|.
name|netty
operator|.
name|util
operator|.
name|internal
operator|.
name|logging
operator|.
name|InternalLoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * This test ensures LogCaptureAppender is configured properly  */
end_comment

begin_class
DECL|class|LogCaptureTest
specifier|public
class|class
name|LogCaptureTest
block|{
annotation|@
name|Test
DECL|method|testCapture ()
specifier|public
name|void
name|testCapture
parameter_list|()
block|{
name|InternalLoggerFactory
operator|.
name|getInstance
argument_list|(
name|ResourceLeakDetector
operator|.
name|class
argument_list|)
operator|.
name|error
argument_list|(
literal|"testError"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|LogCaptureAppender
operator|.
name|getEvents
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|LogCaptureAppender
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

