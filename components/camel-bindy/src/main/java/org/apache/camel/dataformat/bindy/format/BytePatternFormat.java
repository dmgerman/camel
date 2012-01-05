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
name|util
operator|.
name|Locale
import|;
end_import

begin_class
DECL|class|BytePatternFormat
specifier|public
class|class
name|BytePatternFormat
extends|extends
name|NumberPatternFormat
argument_list|<
name|Byte
argument_list|>
block|{
DECL|method|BytePatternFormat ()
specifier|public
name|BytePatternFormat
parameter_list|()
block|{     }
DECL|method|BytePatternFormat (String pattern, Locale locale)
specifier|public
name|BytePatternFormat
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
name|Byte
name|parse
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|super
operator|.
name|getNumberFormat
argument_list|()
operator|.
name|parse
argument_list|(
name|string
argument_list|)
operator|.
name|byteValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit

