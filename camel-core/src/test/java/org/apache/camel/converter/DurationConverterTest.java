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
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|Is
operator|.
name|is
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
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
name|TypeConversionException
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

begin_class
DECL|class|DurationConverterTest
specifier|public
class|class
name|DurationConverterTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testToMillis ()
specifier|public
name|void
name|testToMillis
parameter_list|()
throws|throws
name|Exception
block|{
name|Duration
name|duration
init|=
name|Duration
operator|.
name|parse
argument_list|(
literal|"PT2H6M20.31S"
argument_list|)
decl_stmt|;
name|Long
name|millis
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|long
operator|.
name|class
argument_list|,
name|duration
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|millis
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|millis
argument_list|,
name|is
argument_list|(
literal|7580310L
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToMillisOverflow ()
specifier|public
name|void
name|testToMillisOverflow
parameter_list|()
throws|throws
name|Exception
block|{
name|Duration
name|duration
init|=
name|Duration
operator|.
name|parse
argument_list|(
literal|"P60000000000000D"
argument_list|)
decl_stmt|;
try|try
block|{
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|long
operator|.
name|class
argument_list|,
name|duration
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TypeConversionException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|ArithmeticException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testFromString ()
specifier|public
name|void
name|testFromString
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|durationAsString
init|=
literal|"PT2H6M20.31S"
decl_stmt|;
name|Duration
name|duration
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Duration
operator|.
name|class
argument_list|,
name|durationAsString
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|duration
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|duration
operator|.
name|toString
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"PT2H6M20.31S"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
throws|throws
name|Exception
block|{
name|Duration
name|duration
init|=
name|Duration
operator|.
name|parse
argument_list|(
literal|"PT2H6M20.31S"
argument_list|)
decl_stmt|;
name|String
name|durationAsString
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
name|duration
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|durationAsString
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|durationAsString
argument_list|,
name|is
argument_list|(
literal|"PT2H6M20.31S"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

