begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|rest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
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
name|catalog
operator|.
name|CamelCatalog
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
name|catalog
operator|.
name|DefaultCamelCatalog
import|;
end_import

begin_comment
comment|/**  * A REST based {@link CamelCatalog} service as a JAX-RS resource class.  */
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/camel-catalog"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|class|CamelCatalogRest
specifier|public
class|class
name|CamelCatalogRest
block|{
DECL|field|catalog
specifier|private
name|CamelCatalog
name|catalog
init|=
operator|new
name|DefaultCamelCatalog
argument_list|(
literal|true
argument_list|)
decl_stmt|;
DECL|method|getCatalog ()
specifier|public
name|CamelCatalog
name|getCatalog
parameter_list|()
block|{
return|return
name|catalog
return|;
block|}
comment|/**      * To inject an existing {@link CamelCatalog}      */
DECL|method|setCatalog (CamelCatalog catalog)
specifier|public
name|void
name|setCatalog
parameter_list|(
name|CamelCatalog
name|catalog
parameter_list|)
block|{
name|this
operator|.
name|catalog
operator|=
name|catalog
expr_stmt|;
block|}
comment|/**      * The GET order by id operation      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/findComponentLabels"
argument_list|)
DECL|method|findComponentLabels ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|findComponentLabels
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|findComponentLabels
argument_list|()
return|;
block|}
block|}
end_class

end_unit

