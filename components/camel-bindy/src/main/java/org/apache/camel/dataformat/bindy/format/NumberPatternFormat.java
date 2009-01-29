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
name|DecimalFormat
import|;
end_import

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
name|PatternFormat
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|NumberPatternFormat
specifier|public
specifier|abstract
class|class
name|NumberPatternFormat
parameter_list|<
name|T
parameter_list|>
implements|implements
name|PatternFormat
argument_list|<
name|T
argument_list|>
block|{
DECL|field|pattern
specifier|private
name|String
name|pattern
decl_stmt|;
DECL|method|NumberPatternFormat ()
specifier|public
name|NumberPatternFormat
parameter_list|()
block|{     }
DECL|method|NumberPatternFormat (String pattern)
specifier|public
name|NumberPatternFormat
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
DECL|method|format (T object)
specifier|public
name|String
name|format
parameter_list|(
name|T
name|object
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|this
operator|.
name|pattern
argument_list|,
literal|"pattern"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|getNumberFormat
argument_list|()
operator|.
name|format
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|parse (String string)
specifier|public
name|T
name|parse
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|this
operator|.
name|pattern
argument_list|,
literal|"pattern"
argument_list|)
expr_stmt|;
return|return
operator|(
name|T
operator|)
name|this
operator|.
name|getNumberFormat
argument_list|()
operator|.
name|parse
argument_list|(
name|string
argument_list|)
return|;
block|}
DECL|method|getNumberFormat ()
specifier|protected
name|NumberFormat
name|getNumberFormat
parameter_list|()
block|{
return|return
operator|new
name|DecimalFormat
argument_list|(
name|pattern
argument_list|)
return|;
block|}
DECL|method|getPattern ()
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
DECL|method|setPattern (String pattern)
specifier|public
name|void
name|setPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
block|}
end_class

end_unit

