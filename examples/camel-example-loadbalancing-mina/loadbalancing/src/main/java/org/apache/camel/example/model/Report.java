begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|sun
operator|.
name|tools
operator|.
name|tree
operator|.
name|ThisExpression
import|;
end_import

begin_class
DECL|class|Report
specifier|public
class|class
name|Report
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|id
specifier|private
name|Integer
name|id
decl_stmt|;
DECL|field|title
specifier|private
name|String
name|title
decl_stmt|;
DECL|field|content
specifier|private
name|String
name|content
decl_stmt|;
DECL|field|reply
specifier|private
name|String
name|reply
decl_stmt|;
comment|/** 	 * @return the id 	 */
DECL|method|getId ()
specifier|public
name|Integer
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/** 	 * @param id the id to set 	 */
DECL|method|setId (Integer id)
specifier|public
name|void
name|setId
parameter_list|(
name|Integer
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
comment|/** 	 * @return the title 	 */
DECL|method|getTitle ()
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|title
return|;
block|}
comment|/** 	 * @param title the title to set 	 */
DECL|method|setTitle (String title)
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
comment|/** 	 * @return the content 	 */
DECL|method|getContent ()
specifier|public
name|String
name|getContent
parameter_list|()
block|{
return|return
name|content
return|;
block|}
comment|/** 	 * @param content the content to set 	 */
DECL|method|setContent (String content)
specifier|public
name|void
name|setContent
parameter_list|(
name|String
name|content
parameter_list|)
block|{
name|this
operator|.
name|content
operator|=
name|content
expr_stmt|;
block|}
comment|/** 	 * @return the reply 	 */
DECL|method|getReply ()
specifier|public
name|String
name|getReply
parameter_list|()
block|{
return|return
name|reply
return|;
block|}
comment|/** 	 * @param reply the reply to set 	 */
DECL|method|setReply (String reply)
specifier|public
name|void
name|setReply
parameter_list|(
name|String
name|reply
parameter_list|)
block|{
name|this
operator|.
name|reply
operator|=
name|reply
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|result
operator|.
name|append
argument_list|(
literal|">> ***********************************************"
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
literal|">> Report id : "
operator|+
name|this
operator|.
name|id
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
literal|">> Report title : "
operator|+
name|this
operator|.
name|title
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
literal|">> Report content : "
operator|+
name|this
operator|.
name|content
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
literal|">> Report reply : "
operator|+
name|this
operator|.
name|reply
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
literal|">> ***********************************************"
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

