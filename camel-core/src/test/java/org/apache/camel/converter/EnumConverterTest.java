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
name|LoggingLevel
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
name|TypeConversionException
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|EnumConverterTest
specifier|public
class|class
name|EnumConverterTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testMandatoryConvertEnum ()
specifier|public
name|void
name|testMandatoryConvertEnum
parameter_list|()
throws|throws
name|Exception
block|{
name|LoggingLevel
name|level
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|LoggingLevel
operator|.
name|class
argument_list|,
literal|"DEBUG"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|LoggingLevel
operator|.
name|DEBUG
argument_list|,
name|level
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMandatoryConvertWithExchangeEnum ()
specifier|public
name|void
name|testMandatoryConvertWithExchangeEnum
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
name|LoggingLevel
name|level
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|LoggingLevel
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
literal|"WARN"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|LoggingLevel
operator|.
name|WARN
argument_list|,
name|level
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCaseInsensitive ()
specifier|public
name|void
name|testCaseInsensitive
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
name|LoggingLevel
name|level
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|LoggingLevel
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
literal|"Warn"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|LoggingLevel
operator|.
name|WARN
argument_list|,
name|level
argument_list|)
expr_stmt|;
name|level
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|LoggingLevel
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
literal|"warn"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LoggingLevel
operator|.
name|WARN
argument_list|,
name|level
argument_list|)
expr_stmt|;
name|level
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|LoggingLevel
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
literal|"wARn"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LoggingLevel
operator|.
name|WARN
argument_list|,
name|level
argument_list|)
expr_stmt|;
name|level
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|LoggingLevel
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
literal|"inFO"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
name|level
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMandatoryConvertFailed ()
specifier|public
name|void
name|testMandatoryConvertFailed
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|LoggingLevel
operator|.
name|class
argument_list|,
literal|"XXX"
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
name|TypeConversionException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
end_class

end_unit

