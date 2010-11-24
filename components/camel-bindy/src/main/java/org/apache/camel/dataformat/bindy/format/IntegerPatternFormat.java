begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.format
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|format
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|NumberFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_class
DECL|class|IntegerPatternFormat
specifier|public
class|class
name|IntegerPatternFormat
extends|extends
name|NumberPatternFormat
argument_list|<
name|Integer
argument_list|>
block|{
DECL|method|IntegerPatternFormat ()
specifier|public
name|IntegerPatternFormat
parameter_list|()
block|{     }
DECL|method|IntegerPatternFormat (String pattern, Locale locale)
specifier|public
name|IntegerPatternFormat
parameter_list|(
name|String
name|pattern
parameter_list|,
name|Locale
name|locale
parameter_list|)
block|{
name|super
argument_list|(
name|pattern
argument_list|,
name|locale
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|parse (String string)
specifier|public
name|Integer
name|parse
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|FormatException
block|{
name|Integer
name|res
init|=
literal|null
decl_stmt|;
name|NumberFormat
name|pat
decl_stmt|;
comment|// First we will test if the string can become an Integer
try|try
block|{
name|res
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|string
argument_list|)
expr_stmt|;
comment|// Second, we will parse the string using DecimalPattern
comment|// to apply pattern
name|pat
operator|=
name|super
operator|.
name|getNumberFormat
argument_list|()
expr_stmt|;
name|pat
operator|.
name|parse
argument_list|(
name|string
argument_list|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|FormatException
argument_list|(
literal|"String provided does not fit the Integer pattern defined or is not parseable"
argument_list|)
throw|;
block|}
return|return
name|res
return|;
block|}
block|}
end_class

end_unit

