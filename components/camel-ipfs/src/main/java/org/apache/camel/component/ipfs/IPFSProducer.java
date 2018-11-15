begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ipfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ipfs
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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|component
operator|.
name|ipfs
operator|.
name|IPFSConfiguration
operator|.
name|IPFSCommand
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
name|DefaultProducer
import|;
end_import

begin_class
DECL|class|IPFSProducer
specifier|public
class|class
name|IPFSProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|IPFSProducer (IPFSEndpoint endpoint)
specifier|public
name|IPFSProducer
parameter_list|(
name|IPFSEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|IPFSEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|IPFSEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
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
name|IPFSEndpoint
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
name|IPFSCommand
name|cmd
init|=
name|endpoint
operator|.
name|getCommand
argument_list|()
decl_stmt|;
if|if
condition|(
name|IPFSCommand
operator|.
name|version
operator|==
name|cmd
condition|)
block|{
name|String
name|resp
init|=
name|endpoint
operator|.
name|ipfsVersion
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|resp
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|IPFSCommand
operator|.
name|add
operator|==
name|cmd
condition|)
block|{
name|Path
name|path
init|=
name|pathFromBody
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|cids
init|=
name|endpoint
operator|.
name|ipfsAdd
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Object
name|resp
init|=
name|cids
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|toFile
argument_list|()
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|resp
operator|=
name|cids
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|?
name|cids
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|resp
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|IPFSCommand
operator|.
name|cat
operator|==
name|cmd
condition|)
block|{
name|String
name|cid
init|=
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|InputStream
name|resp
init|=
name|endpoint
operator|.
name|ipfsCat
argument_list|(
name|cid
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|resp
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|IPFSCommand
operator|.
name|get
operator|==
name|cmd
condition|)
block|{
name|Path
name|outdir
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOutdir
argument_list|()
decl_stmt|;
name|String
name|cid
init|=
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Path
name|resp
init|=
name|endpoint
operator|.
name|ipfsGet
argument_list|(
name|cid
argument_list|,
name|outdir
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|resp
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|cmd
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|pathFromBody (Exchange exchange)
specifier|private
name|Path
name|pathFromBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|Path
condition|)
block|{
return|return
operator|(
name|Path
operator|)
name|body
return|;
block|}
if|if
condition|(
name|body
operator|instanceof
name|String
condition|)
block|{
return|return
name|Paths
operator|.
name|get
argument_list|(
operator|(
name|String
operator|)
name|body
argument_list|)
return|;
block|}
if|if
condition|(
name|body
operator|instanceof
name|File
condition|)
block|{
return|return
operator|(
operator|(
name|File
operator|)
name|body
operator|)
operator|.
name|toPath
argument_list|()
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid path: "
operator|+
name|body
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

