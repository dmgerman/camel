begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ExchangeException
import|;
end_import

begin_class
DECL|class|BeanWithExchangeExceptionAnnotationWrappedExceptionTest
specifier|public
class|class
name|BeanWithExchangeExceptionAnnotationWrappedExceptionTest
extends|extends
name|BeanWithExchangeExceptionAnnotationTest
block|{
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|method|throwException ()
specifier|public
name|void
name|throwException
parameter_list|()
throws|throws
name|Exception
block|{
comment|// wrap the problem in an IO exception
name|IOException
name|io
init|=
operator|new
name|IOException
argument_list|(
literal|"Forced"
argument_list|)
decl_stmt|;
name|io
operator|.
name|initCause
argument_list|(
operator|new
name|MyCustomException
argument_list|(
literal|"I'm being thrown!!"
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|io
throw|;
block|}
comment|// to unit test that we can set a type to the @ExchangeException that we want this caused by exception
comment|// in the exception hieracy
DECL|method|handleException (@xchangeException MyCustomException custom)
specifier|public
name|void
name|handleException
parameter_list|(
annotation|@
name|ExchangeException
name|MyCustomException
name|custom
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|custom
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"I'm being thrown!!"
argument_list|,
name|custom
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

