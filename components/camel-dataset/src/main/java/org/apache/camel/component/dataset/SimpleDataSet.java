begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dataset
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dataset
package|;
end_package

begin_comment
comment|/**  * A simple DataSet that allows a static payload to be used to create each message exchange  * along with using a pluggable transformer to randomize the message.  */
end_comment

begin_class
DECL|class|SimpleDataSet
specifier|public
class|class
name|SimpleDataSet
extends|extends
name|DataSetSupport
block|{
DECL|field|defaultBody
specifier|private
name|Object
name|defaultBody
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
DECL|method|SimpleDataSet ()
specifier|public
name|SimpleDataSet
parameter_list|()
block|{     }
DECL|method|SimpleDataSet (int size)
specifier|public
name|SimpleDataSet
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|super
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getDefaultBody ()
specifier|public
name|Object
name|getDefaultBody
parameter_list|()
block|{
return|return
name|defaultBody
return|;
block|}
DECL|method|setDefaultBody (Object defaultBody)
specifier|public
name|void
name|setDefaultBody
parameter_list|(
name|Object
name|defaultBody
parameter_list|)
block|{
name|this
operator|.
name|defaultBody
operator|=
name|defaultBody
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
comment|/**      * Creates the message body for a given message      */
annotation|@
name|Override
DECL|method|createMessageBody (long messageIndex)
specifier|protected
name|Object
name|createMessageBody
parameter_list|(
name|long
name|messageIndex
parameter_list|)
block|{
return|return
name|getDefaultBody
argument_list|()
return|;
block|}
block|}
end_class

end_unit

