begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_expr_stmt
unit|#
operator|#
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
expr|#
operator|#
name|Licensed
name|to
name|the
name|Apache
name|Software
name|Foundation
argument_list|(
name|ASF
argument_list|)
name|under
name|one
name|or
name|more
expr|#
operator|#
name|contributor
name|license
name|agreements
operator|.
name|See
name|the
name|NOTICE
name|file
name|distributed
name|with
expr|#
operator|#
name|this
name|work
end_expr_stmt

begin_for
for|for additional information regarding copyright ownership. ## The ASF licenses this file to You under the Apache License
operator|,
name|Version
literal|2.0
expr|#
operator|#
operator|(
name|the
literal|"License"
operator|)
expr_stmt|;
end_for

begin_expr_stmt
name|you
name|may
name|not
name|use
name|this
name|file
name|except
name|in
name|compliance
name|with
expr|#
operator|#
name|the
name|License
operator|.
name|You
name|may
name|obtain
name|a
name|copy
name|of
name|the
name|License
name|at
expr|#
operator|#
expr|#
operator|#
name|http
operator|:
comment|//www.apache.org/licenses/LICENSE-2.0
expr|#
operator|#
expr|#
operator|#
name|Unless
name|required
name|by
name|applicable
name|law
name|or
name|agreed
name|to
name|in
name|writing
operator|,
name|software
expr|#
operator|#
name|distributed
name|under
name|the
name|License
name|is
name|distributed
name|on
name|an
literal|"AS IS"
name|BASIS
operator|,
expr_stmt|#
operator|#
name|WITHOUT
name|WARRANTIES
name|OR
name|CONDITIONS
name|OF
name|ANY
name|KIND
operator|,
name|either
name|express
name|or
name|implied
operator|.
expr|#
operator|#
name|See
name|the
name|License
end_expr_stmt

begin_for
for|for the specific language governing permissions and ## limitations under the License. ## ------------------------------------------------------------------------ package $
block|{
package|package
block|}
end_for

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
operator|.
name|CamelBlueprintTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|BlueprintBeanPropertiesOverrideFromFileRouteTest
specifier|public
class|class
name|BlueprintBeanPropertiesOverrideFromFileRouteTest
extends|extends
name|CamelBlueprintTestSupport
block|{
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"/OSGI-INF/blueprint/blueprint-bean.xml"
return|;
block|}
annotation|@
name|Override
DECL|method|loadConfigAdminConfigurationFile ()
specifier|protected
name|String
index|[]
name|loadConfigAdminConfigurationFile
parameter_list|()
block|{
comment|// which .cfg file to use, and the name of the persistence-id
return|return
operator|new
name|String
index|[]
block|{
literal|"src/test/resources/etc/HelloBean.cfg"
block|,
literal|"HelloBean"
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testRoute ()
specifier|public
name|void
name|testRoute
parameter_list|()
throws|throws
name|Exception
block|{
comment|// the route is timer based, so every 5th second a message is send
comment|// we should then expect at least one message
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// assert expectations
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

