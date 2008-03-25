begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jhc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jhc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|io
operator|.
name|UnsupportedEncodingException
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
name|Converter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|InputStreamEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|StringEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|EntityUtils
import|;
end_import

begin_comment
comment|/**  * Created by IntelliJ IDEA.  * User: gnodet  * Date: Sep 10, 2007  * Time: 8:26:44 AM  * To change this template use File | Settings | File Templates.  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|JhcConverter
specifier|public
specifier|final
class|class
name|JhcConverter
block|{
DECL|method|JhcConverter ()
specifier|private
name|JhcConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toInputStream (HttpEntity entity)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|HttpEntity
name|entity
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|entity
operator|.
name|getContent
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteArray (HttpEntity entity)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|HttpEntity
name|entity
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|EntityUtils
operator|.
name|toByteArray
argument_list|(
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (HttpEntity entity)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|HttpEntity
name|entity
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|EntityUtils
operator|.
name|toString
argument_list|(
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toEntity (InputStream is)
specifier|public
specifier|static
name|HttpEntity
name|toEntity
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
return|return
operator|new
name|InputStreamEntity
argument_list|(
name|is
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toEntity (String str)
specifier|public
specifier|static
name|HttpEntity
name|toEntity
parameter_list|(
name|String
name|str
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
return|return
operator|new
name|StringEntity
argument_list|(
name|str
argument_list|)
return|;
block|}
block|}
end_class

end_unit

