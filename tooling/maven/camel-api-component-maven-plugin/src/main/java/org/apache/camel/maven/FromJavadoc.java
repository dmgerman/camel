begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

begin_comment
comment|/**  * Javadoc API generator properties.  */
end_comment

begin_class
DECL|class|FromJavadoc
specifier|public
class|class
name|FromJavadoc
block|{
DECL|field|excludePackages
specifier|protected
name|String
name|excludePackages
init|=
name|JavadocApiMethodGeneratorMojo
operator|.
name|DEFAULT_EXCLUDE_PACKAGES
decl_stmt|;
DECL|field|excludeClasses
specifier|protected
name|String
name|excludeClasses
decl_stmt|;
DECL|field|excludeMethods
specifier|protected
name|String
name|excludeMethods
decl_stmt|;
DECL|method|getExcludePackages ()
specifier|public
name|String
name|getExcludePackages
parameter_list|()
block|{
return|return
name|excludePackages
return|;
block|}
DECL|method|setExcludePackages (String excludePackages)
specifier|public
name|void
name|setExcludePackages
parameter_list|(
name|String
name|excludePackages
parameter_list|)
block|{
name|this
operator|.
name|excludePackages
operator|=
name|excludePackages
expr_stmt|;
block|}
DECL|method|getExcludeClasses ()
specifier|public
name|String
name|getExcludeClasses
parameter_list|()
block|{
return|return
name|excludeClasses
return|;
block|}
DECL|method|setExcludeClasses (String excludeClasses)
specifier|public
name|void
name|setExcludeClasses
parameter_list|(
name|String
name|excludeClasses
parameter_list|)
block|{
name|this
operator|.
name|excludeClasses
operator|=
name|excludeClasses
expr_stmt|;
block|}
DECL|method|getExcludeMethods ()
specifier|public
name|String
name|getExcludeMethods
parameter_list|()
block|{
return|return
name|excludeMethods
return|;
block|}
DECL|method|setExcludeMethods (String excludeMethods)
specifier|public
name|void
name|setExcludeMethods
parameter_list|(
name|String
name|excludeMethods
parameter_list|)
block|{
name|this
operator|.
name|excludeMethods
operator|=
name|excludeMethods
expr_stmt|;
block|}
block|}
end_class

end_unit

