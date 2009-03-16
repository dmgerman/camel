begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|spi
operator|.
name|PackageScanFilter
import|;
end_import

begin_comment
comment|/**  * Package scan filter for testing if a given class is assignable to another class.  */
end_comment

begin_class
DECL|class|AssignableToPackageScanFilter
specifier|public
class|class
name|AssignableToPackageScanFilter
implements|implements
name|PackageScanFilter
block|{
DECL|field|parent
specifier|private
specifier|final
name|Class
name|parent
decl_stmt|;
DECL|method|AssignableToPackageScanFilter (Class parentType)
specifier|public
name|AssignableToPackageScanFilter
parameter_list|(
name|Class
name|parentType
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parentType
expr_stmt|;
block|}
DECL|method|matches (Class type)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
return|return
name|type
operator|!=
literal|null
operator|&&
name|parent
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
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
literal|"is assignable to "
operator|+
name|parent
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

