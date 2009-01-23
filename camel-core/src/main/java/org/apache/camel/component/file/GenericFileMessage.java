begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
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
name|impl
operator|.
name|DefaultMessage
import|;
end_import

begin_comment
comment|/**  * Remote file message  */
end_comment

begin_class
DECL|class|GenericFileMessage
specifier|public
class|class
name|GenericFileMessage
parameter_list|<
name|T
parameter_list|>
extends|extends
name|DefaultMessage
block|{
DECL|field|genericFile
specifier|private
name|GenericFile
name|genericFile
decl_stmt|;
DECL|method|GenericFileMessage ()
specifier|public
name|GenericFileMessage
parameter_list|()
block|{     }
DECL|method|GenericFileMessage (GenericFile genericFile)
specifier|public
name|GenericFileMessage
parameter_list|(
name|GenericFile
name|genericFile
parameter_list|)
block|{
name|this
operator|.
name|genericFile
operator|=
name|genericFile
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getExchange ()
specifier|public
name|GenericFileExchange
name|getExchange
parameter_list|()
block|{
return|return
operator|(
name|GenericFileExchange
operator|)
name|super
operator|.
name|getExchange
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createBody ()
specifier|protected
name|Object
name|createBody
parameter_list|()
block|{
return|return
name|genericFile
operator|.
name|getBody
argument_list|()
return|;
block|}
DECL|method|getGenericFile ()
specifier|public
name|GenericFile
argument_list|<
name|T
argument_list|>
name|getGenericFile
parameter_list|()
block|{
return|return
name|genericFile
return|;
block|}
DECL|method|setRemoteFile (GenericFile genericFile)
specifier|public
name|void
name|setRemoteFile
parameter_list|(
name|GenericFile
name|genericFile
parameter_list|)
block|{
name|this
operator|.
name|genericFile
operator|=
name|genericFile
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|GenericFileMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|GenericFileMessage
argument_list|()
return|;
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
literal|"GenericFileMessage: "
operator|+
name|genericFile
return|;
block|}
block|}
end_class

end_unit

