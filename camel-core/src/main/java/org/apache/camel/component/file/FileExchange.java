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
name|java
operator|.
name|io
operator|.
name|File
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
name|CamelContext
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

begin_comment
comment|/**  * A {@link Exchange} for File  *  * @version $Revision: 520985 $  */
end_comment

begin_class
DECL|class|FileExchange
specifier|public
class|class
name|FileExchange
extends|extends
name|DefaultExchange
block|{
DECL|field|file
specifier|private
name|File
name|file
decl_stmt|;
DECL|method|FileExchange (CamelContext camelContext, File file)
specifier|public
name|FileExchange
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|File
name|file
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|setIn
argument_list|(
operator|new
name|FileMessage
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
comment|/**      * @return the file      */
DECL|method|getFile ()
specifier|public
name|File
name|getFile
parameter_list|()
block|{
return|return
name|this
operator|.
name|file
return|;
block|}
comment|/**      * @param file the file to set      */
DECL|method|setFile (File file)
specifier|public
name|void
name|setFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
block|}
end_class

end_unit

