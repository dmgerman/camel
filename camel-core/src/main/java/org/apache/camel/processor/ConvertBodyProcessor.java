begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|Message
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
name|Processor
import|;
end_import

begin_comment
comment|/**  * A processor which converts the payload of the input message to be of the given type  *<p/>  * If the convertions fails an {@link org.apache.camel.InvalidPayloadException} is thrown.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ConvertBodyProcessor
specifier|public
class|class
name|ConvertBodyProcessor
implements|implements
name|Processor
block|{
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|charset
specifier|private
specifier|final
name|String
name|charset
decl_stmt|;
DECL|method|ConvertBodyProcessor (Class<?> type)
specifier|public
name|ConvertBodyProcessor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|charset
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|ConvertBodyProcessor (Class<?> type, String charset)
specifier|public
name|ConvertBodyProcessor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|charset
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|charset
operator|=
name|charset
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"convertBodyTo["
operator|+
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|charset
argument_list|)
expr_stmt|;
block|}
name|Object
name|value
init|=
name|in
operator|.
name|getMandatoryBody
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|copyFrom
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|out
operator|.
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|in
operator|.
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
block|}
end_class

end_unit

