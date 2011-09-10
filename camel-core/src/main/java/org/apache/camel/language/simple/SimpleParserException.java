begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
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
name|RuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * Holds information about error parsing the simple expression at a given location  * in the input.  */
end_comment

begin_class
DECL|class|SimpleParserException
specifier|public
class|class
name|SimpleParserException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|index
specifier|private
specifier|final
name|int
name|index
decl_stmt|;
DECL|method|SimpleParserException (String message, int index)
specifier|public
name|SimpleParserException
parameter_list|(
name|String
name|message
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
block|}
comment|/**      * Index where the parsing error occurred      *      * @return index of the parsing error in the input      */
DECL|method|getIndex ()
specifier|public
name|int
name|getIndex
parameter_list|()
block|{
return|return
name|index
return|;
block|}
block|}
end_class

end_unit

