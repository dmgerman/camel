begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
name|Future
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
name|ContextTestSupport
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
name|Exchange
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
name|NoTypeConversionAvailableException
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
name|builder
operator|.
name|RouteBuilder
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
name|impl
operator|.
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|FutureConverterTest
specifier|public
class|class
name|FutureConverterTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testConvertFuture ()
specifier|public
name|void
name|testConvertFuture
parameter_list|()
block|{
name|Future
argument_list|<
name|?
argument_list|>
name|future
init|=
name|template
operator|.
name|asyncRequestBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|String
name|out
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|future
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testConvertMandatoryFuture ()
specifier|public
name|void
name|testConvertMandatoryFuture
parameter_list|()
throws|throws
name|Exception
block|{
name|Future
argument_list|<
name|?
argument_list|>
name|future
init|=
name|template
operator|.
name|asyncRequestBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|String
name|out
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|future
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testConvertMandatoryFutureWithExchange ()
specifier|public
name|void
name|testConvertMandatoryFutureWithExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Future
argument_list|<
name|?
argument_list|>
name|future
init|=
name|template
operator|.
name|asyncRequestBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|String
name|out
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|future
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testConvertMandatoryFutureWithExchangeFailed ()
specifier|public
name|void
name|testConvertMandatoryFutureWithExchangeFailed
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Future
argument_list|<
name|?
argument_list|>
name|future
init|=
name|template
operator|.
name|asyncRequestBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
try|try
block|{
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|Timestamp
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|future
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
DECL|method|testConvertFutureWithExchangeFailed ()
specifier|public
name|void
name|testConvertFutureWithExchangeFailed
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Future
argument_list|<
name|?
argument_list|>
name|future
init|=
name|template
operator|.
name|asyncRequestBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|Timestamp
name|out
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Timestamp
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|future
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testConvertFutureCancelled ()
specifier|public
name|void
name|testConvertFutureCancelled
parameter_list|()
block|{
name|Future
argument_list|<
name|?
argument_list|>
name|future
init|=
name|template
operator|.
name|asyncRequestBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|out
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|future
argument_list|)
decl_stmt|;
comment|// should be null since its cancelled
name|assertNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testConvertFutureCancelledThenOkay ()
specifier|public
name|void
name|testConvertFutureCancelledThenOkay
parameter_list|()
block|{
name|Future
argument_list|<
name|?
argument_list|>
name|future
init|=
name|template
operator|.
name|asyncRequestBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|out
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|future
argument_list|)
decl_stmt|;
comment|// should be null since its cancelled
name|assertNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|future
operator|=
name|template
operator|.
name|asyncRequestBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|out
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|future
argument_list|)
expr_stmt|;
comment|// not cancelled so we get the result this time
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|delay
argument_list|(
literal|500
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

