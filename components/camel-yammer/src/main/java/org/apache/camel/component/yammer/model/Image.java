begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yammer
operator|.
name|model
package|;
end_package

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonIgnoreProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonProperty
import|;
end_import

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|Image
specifier|public
class|class
name|Image
block|{
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"thumbnail_url"
argument_list|)
DECL|field|thumbnailUrl
specifier|private
name|String
name|thumbnailUrl
decl_stmt|;
DECL|field|size
specifier|private
name|Long
name|size
decl_stmt|;
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
DECL|method|setUrl (String url)
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getThumbnailUrl ()
specifier|public
name|String
name|getThumbnailUrl
parameter_list|()
block|{
return|return
name|thumbnailUrl
return|;
block|}
DECL|method|setThumbnailUrl (String thumbnailUrl)
specifier|public
name|void
name|setThumbnailUrl
parameter_list|(
name|String
name|thumbnailUrl
parameter_list|)
block|{
name|this
operator|.
name|thumbnailUrl
operator|=
name|thumbnailUrl
expr_stmt|;
block|}
DECL|method|getSize ()
specifier|public
name|Long
name|getSize
parameter_list|()
block|{
return|return
name|size
return|;
block|}
DECL|method|setSize (Long size)
specifier|public
name|void
name|setSize
parameter_list|(
name|Long
name|size
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
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
literal|"Image [url="
operator|+
name|url
operator|+
literal|", thumbnailUrl="
operator|+
name|thumbnailUrl
operator|+
literal|", size="
operator|+
name|size
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

