begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|spi
operator|.
name|Label
import|;
end_import

begin_comment
comment|/**  * Scans for Java {@link org.apache.camel.builder.RouteBuilder} classes in java packages  */
end_comment

begin_class
annotation|@
name|Label
argument_list|(
literal|"configuration"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"packageScan"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|PackageScanDefinition
specifier|public
class|class
name|PackageScanDefinition
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"package"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|packages
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|packages
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
DECL|field|excludes
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|excludes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
DECL|field|includes
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|includes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|PackageScanDefinition ()
specifier|public
name|PackageScanDefinition
parameter_list|()
block|{     }
DECL|method|getExcludes ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExcludes
parameter_list|()
block|{
return|return
name|excludes
return|;
block|}
DECL|method|getIncludes ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIncludes
parameter_list|()
block|{
return|return
name|includes
return|;
block|}
DECL|method|getPackages ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPackages
parameter_list|()
block|{
return|return
name|packages
return|;
block|}
DECL|method|setPackages (List<String> packages)
specifier|public
name|void
name|setPackages
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|packages
parameter_list|)
block|{
name|this
operator|.
name|packages
operator|=
name|packages
expr_stmt|;
block|}
DECL|method|setExcludes (List<String> excludes)
specifier|public
name|void
name|setExcludes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|excludes
parameter_list|)
block|{
name|this
operator|.
name|excludes
operator|=
name|excludes
expr_stmt|;
block|}
DECL|method|setIncludes (List<String> includes)
specifier|public
name|void
name|setIncludes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|includes
parameter_list|)
block|{
name|this
operator|.
name|includes
operator|=
name|includes
expr_stmt|;
block|}
DECL|method|clear ()
specifier|protected
name|void
name|clear
parameter_list|()
block|{
name|packages
operator|.
name|clear
argument_list|()
expr_stmt|;
name|excludes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|includes
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

