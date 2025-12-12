package design.ore.forge.api.registrations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class Registration
{
    protected String name;
    @Setter protected String targetUrl;
}
