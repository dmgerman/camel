begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.digitalocean.producer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|digitalocean
operator|.
name|producer
package|;
end_package

begin_import
import|import
name|com
operator|.
name|myjeeva
operator|.
name|digitalocean
operator|.
name|pojo
operator|.
name|Delete
import|;
end_import

begin_import
import|import
name|com
operator|.
name|myjeeva
operator|.
name|digitalocean
operator|.
name|pojo
operator|.
name|Tag
import|;
end_import

begin_import
import|import
name|com
operator|.
name|myjeeva
operator|.
name|digitalocean
operator|.
name|pojo
operator|.
name|Tags
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
name|digitalocean
operator|.
name|DigitalOceanConfiguration
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
name|digitalocean
operator|.
name|DigitalOceanEndpoint
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
name|digitalocean
operator|.
name|constants
operator|.
name|DigitalOceanHeaders
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

begin_comment
comment|/**  * The DigitalOcean producer for Tags API.  */
end_comment

begin_class
DECL|class|DigitalOceanTagsProducer
specifier|public
class|class
name|DigitalOceanTagsProducer
extends|extends
name|DigitalOceanProducer
block|{
DECL|method|DigitalOceanTagsProducer (DigitalOceanEndpoint endpoint, DigitalOceanConfiguration configuration)
specifier|public
name|DigitalOceanTagsProducer
parameter_list|(
name|DigitalOceanEndpoint
name|endpoint
parameter_list|,
name|DigitalOceanConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
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
switch|switch
condition|(
name|determineOperation
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
case|case
name|list
case|:
name|getTags
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|create
case|:
name|createTag
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|get
case|:
name|getTag
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|delete
case|:
name|deleteTag
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported operation"
argument_list|)
throw|;
block|}
block|}
DECL|method|createTag (Exchange exchange)
specifier|private
name|void
name|createTag
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DigitalOceanHeaders
operator|.
name|NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|DigitalOceanHeaders
operator|.
name|NAME
operator|+
literal|" must be specified"
argument_list|)
throw|;
block|}
name|Tag
name|tag
init|=
name|getEndpoint
argument_list|()
operator|.
name|getDigitalOceanClient
argument_list|()
operator|.
name|createTag
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Create Tag [{}] "
argument_list|,
name|tag
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|tag
argument_list|)
expr_stmt|;
block|}
DECL|method|getTag (Exchange exchange)
specifier|private
name|void
name|getTag
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DigitalOceanHeaders
operator|.
name|NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|DigitalOceanHeaders
operator|.
name|NAME
operator|+
literal|" must be specified"
argument_list|)
throw|;
block|}
name|Tag
name|tag
init|=
name|getEndpoint
argument_list|()
operator|.
name|getDigitalOceanClient
argument_list|()
operator|.
name|getTag
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Tag [{}] "
argument_list|,
name|tag
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|tag
argument_list|)
expr_stmt|;
block|}
DECL|method|getTags (Exchange exchange)
specifier|private
name|void
name|getTags
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Tags
name|tags
init|=
name|getEndpoint
argument_list|()
operator|.
name|getDigitalOceanClient
argument_list|()
operator|.
name|getAvailableTags
argument_list|(
name|configuration
operator|.
name|getPage
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPerPage
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"All Tags : page {} / {} per page [{}] "
argument_list|,
name|configuration
operator|.
name|getPage
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPerPage
argument_list|()
argument_list|,
name|tags
operator|.
name|getTags
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|tags
operator|.
name|getTags
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteTag (Exchange exchange)
specifier|private
name|void
name|deleteTag
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DigitalOceanHeaders
operator|.
name|NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|DigitalOceanHeaders
operator|.
name|NAME
operator|+
literal|" must be specified"
argument_list|)
throw|;
block|}
name|Delete
name|delete
init|=
name|getEndpoint
argument_list|()
operator|.
name|getDigitalOceanClient
argument_list|()
operator|.
name|deleteTag
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Delete Tag [{}] "
argument_list|,
name|delete
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|delete
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

